package ru.itdt.fileconverter.Benchmark;

import ru.itdt.fileconverter.bean.json.JsonRoot;
import ru.itdt.fileconverter.converters.XmlToJson;
import ru.itdt.fileconverter.readers.xml.SaxReader;
import ru.itdt.fileconverter.writers.Writer;
import ru.itdt.fileconverter.writers.json.GsonWriter;
import ru.itdt.fileconverter.writers.json.JacksonWriter;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Проверка времени работы Jackson и Gson для создания файла.
 */
public class JsonWriters {
    @State(Scope.Thread)
    public static class MyState {
        private final String pathToFile = "src/test/resources/NewJson.json";
        private final Writer<JsonRoot> jacksonWriter = new JacksonWriter();
        private final Writer<JsonRoot> gsonWriter = new GsonWriter();
        private JsonRoot upper;

        @Setup
        public void setup() throws ParserConfigurationException, IOException, SAXException {
            upper = new XmlToJson()
                .convert(
                    new SaxReader()
                        .parse("src/test/resources/TestInput.xml"));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void writeJackson(final MyState state)
        throws JAXBException, IOException, XMLStreamException {
        state.jacksonWriter.write(state.upper, state.pathToFile);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void writeGson(final MyState state)
        throws JAXBException, IOException, XMLStreamException {
        state.gsonWriter.write(state.upper,state.pathToFile);
    }


    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder()
            .include(JsonWriters.class.getSimpleName())
            .forks(1)
            .build()).run();

    }
}
