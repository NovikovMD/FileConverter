package jmh;

import fileconverter.bean.xml.XmlUpper;
import fileconverter.converters.JsonToXml;
import fileconverter.readers.json.GsonReader;
import fileconverter.writers.Writer;
import fileconverter.writers.xml.JaxbWriter;
import fileconverter.writers.xml.StaxWriter;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class XmlWriters {

    @State(Scope.Thread)
    public static class MyState {
        public final String pathToFile = "src/test/resources/NewXML.xml";
        public final Writer<XmlUpper> staxWriter = new StaxWriter();
        public final Writer<XmlUpper> jaxbWriter;

        {
            try {
                jaxbWriter = new JaxbWriter();
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }

        public XmlUpper upper;

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
        Options opt = new OptionsBuilder()
            .include(XmlWriters .class.getSimpleName())
            .forks(1)
            .build();

        new Runner(opt).run();
    }
}
