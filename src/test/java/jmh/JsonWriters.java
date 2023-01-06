package jmh;

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

public class JsonWriters {
    @State(Scope.Thread)
    public static class MyState {
        public final String pathToFile = "src/test/resources/NewJson.json";
        public final Writer<JsonRoot> jacksonWriter = new JacksonWriter();
        public final Writer<JsonRoot> gsonWriter = new GsonWriter();
        public JsonRoot upper;

        @Setup
        public void setup() throws ParserConfigurationException, IOException, SAXException {
            upper = new XmlToJson()
                .convert(
                    new SaxReader()
                        .parse("src/test/resources/TestInput.xml"));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void writeJackson(MyState state)
        throws JAXBException, IOException, XMLStreamException {
        state.jacksonWriter.write(state.upper, state.pathToFile);
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void writeGson(MyState state)
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
