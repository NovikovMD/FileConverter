package jmh;

import ru.itdt.fileconverter.bean.xml.XmlRoot;
import ru.itdt.fileconverter.converters.JsonToXml;
import ru.itdt.fileconverter.readers.json.GsonReader;
import ru.itdt.fileconverter.writers.Writer;
import ru.itdt.fileconverter.writers.xml.JaxbWriter;
import ru.itdt.fileconverter.writers.xml.StaxWriter;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class XmlWriters {

    @State(Scope.Thread)
    public static class MyState {
        public final String pathToFile = "src/test/resources/NewXML.xml";
        public final Writer<XmlRoot> staxWriter = new StaxWriter();
        public final Writer<XmlRoot> jaxbWriter;
        public XmlRoot upper;

        {
            try {
                jaxbWriter = new JaxbWriter();
            } catch (JAXBException exception) {
                throw new AssertionError();
            }
        }

        @Setup
        public void setup() throws IOException {
            upper = new JsonToXml()
                .convert(
                    new GsonReader()
                        .parse("src/test/resources/TestInput.json"));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void writeStax(MyState state)
        throws JAXBException, IOException, XMLStreamException {
        state.staxWriter.write(state.upper, state.pathToFile);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void writeJaxb(MyState state)
        throws JAXBException, IOException, XMLStreamException {
        state.jaxbWriter.write(state.upper,state.pathToFile);
    }


    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder()
            .include(XmlWriters .class.getSimpleName())
            .forks(1)
            .build()).run();
    }
}
