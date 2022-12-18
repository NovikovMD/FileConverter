package jmh;

import fileconverter.bean.json.JsonUpper;
import fileconverter.converters.XmlToJson;
import fileconverter.readers.xml.SaxReader;
import fileconverter.writers.Writer;
import fileconverter.writers.json.GsonWriter;
import fileconverter.writers.json.JacksonWriter;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
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
        public final Writer<JsonUpper> jacksonWriter = new JacksonWriter();
        public final Writer<JsonUpper> gsonWriter = new GsonWriter();
        public JsonUpper upper;

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
        Options opt = new OptionsBuilder()
            .include(JsonWriters.class.getSimpleName())
            .forks(1)
            .build();

        new Runner(opt).run();

    }
}
