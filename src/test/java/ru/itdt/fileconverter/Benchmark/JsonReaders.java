package ru.itdt.fileconverter.Benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.xml.sax.SAXException;
import ru.itdt.fileconverter.bean.json.JsonRoot;
import ru.itdt.fileconverter.readers.Reader;
import ru.itdt.fileconverter.readers.json.GsonReader;
import ru.itdt.fileconverter.readers.json.JacksonReader;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Проверка времени работы Jackson и Gson для считывания файла.
 */
public class JsonReaders {
    @State(Scope.Thread)
    public static class MyState {
        private final String pathToFile = "src/test/resources/TestInput.json";
        private final Reader<JsonRoot> jacksonReader = new JacksonReader();
        private final Reader<JsonRoot> gsonReader = new GsonReader();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void readJackson(final MyState state,final Blackhole blackhole)
        throws JAXBException, ParserConfigurationException, IOException, SAXException {
        blackhole.consume(state.jacksonReader.parse(state.pathToFile));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void readGson(final MyState state,final Blackhole blackhole)
        throws JAXBException, ParserConfigurationException, IOException, SAXException {
        blackhole.consume(state.gsonReader.parse(state.pathToFile));
    }



    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder()
            .include(JsonReaders.class.getSimpleName())
            .forks(1)
            .build()).run();
    }
}
