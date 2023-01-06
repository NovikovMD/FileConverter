package jmh;

import ru.itdt.fileconverter.bean.xml.XmlRoot;
import ru.itdt.fileconverter.readers.Reader;
import ru.itdt.fileconverter.readers.xml.JaxbReader;
import ru.itdt.fileconverter.readers.xml.SaxReader;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class XmlReaders {

    @State(Scope.Thread)
    public static class MyState {
        public final String pathToFile = "src/test/resources/TestInput.xml";
        public final Reader<XmlRoot> saxReader = new SaxReader();
        public final Reader<XmlRoot> jaxbReader;

        {
            try {
                jaxbReader = new JaxbReader();
            } catch (JAXBException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void readSax(MyState state, Blackhole blackhole)
        throws JAXBException, ParserConfigurationException, IOException, SAXException {
        blackhole.consume(state.saxReader.parse(state.pathToFile));
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void readJaxb(MyState state, Blackhole blackhole)
        throws JAXBException, ParserConfigurationException, IOException, SAXException {
        blackhole.consume(state.jaxbReader.parse(state.pathToFile));
    }


    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder()
            .include(XmlReaders.class.getSimpleName())
            .forks(1)
            .build()).run();
    }
}
