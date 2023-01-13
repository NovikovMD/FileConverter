package ru.itdt.fileconverter.Benchmark;

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

/**
 * Проверка времени работы Stax и Jaxb для создания файла.
 */
public class XmlWriters {

    @State(Scope.Thread)
    public static class MyState {
        private final String pathToSecondFile = "src/test/resources/NewXML.xml";
        private final Writer<XmlRoot> staxWriter = new StaxWriter();
        private final Writer<XmlRoot> jaxbWriter;
        private XmlRoot upper;

        {
            try {
                jaxbWriter = new JaxbWriter();
            } catch (JAXBException exception) {
                throw new AssertionError(exception);
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
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void writeStax(final MyState state)
        throws JAXBException, IOException, XMLStreamException {
        state.staxWriter.write(state.upper, state.pathToSecondFile);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void writeJaxb(final MyState state)
        throws JAXBException, IOException, XMLStreamException {
        state.jaxbWriter.write(state.upper,state.pathToSecondFile);
    }


    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder()
            .include(XmlWriters .class.getSimpleName())
            .forks(1)
            .build()).run();
    }
}
