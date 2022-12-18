package jmh;

import fileconverter.bean.xml.XmlUpper;
import fileconverter.readers.Reader;
import fileconverter.readers.xml.JaxbReader;
import fileconverter.readers.xml.SaxReader;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
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
        public final Reader<XmlUpper> saxReader = new SaxReader();
        public final Reader<XmlUpper> jaxbReader;

        {
            try {
                jaxbReader = new JaxbReader();
            } catch (JAXBException e) {
                throw new RuntimeException(e);
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
        Options opt = new OptionsBuilder()
            .include(XmlReaders.class.getSimpleName())
            .forks(1)
            .build();

        new Runner(opt).run();
    }
}
