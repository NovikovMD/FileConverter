package jmh;

import fileconverter.bean.json.JsonUpper;
import fileconverter.readers.Reader;
import fileconverter.readers.json.GsonReader;
import fileconverter.readers.json.JacksonReader;
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

public class JsonReaders {
    @State(Scope.Thread)
    public static class MyState {
        public final String pathToFile = "src/test/resources/TestInput.json";
        public final Reader<JsonUpper> jacksonReader = new JacksonReader();
        public final Reader<JsonUpper> gsonReader = new GsonReader();
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void readJackson(MyState state, Blackhole blackhole)
        throws JAXBException, ParserConfigurationException, IOException, SAXException {
        blackhole.consume(state.jacksonReader.parse(state.pathToFile));
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void readGson(MyState state, Blackhole blackhole)
        throws JAXBException, ParserConfigurationException, IOException, SAXException {
        blackhole.consume(state.gsonReader.parse(state.pathToFile));
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(JsonReaders.class.getSimpleName())
            .forks(1)
            .build();

        new Runner(opt).run();
    }
}
