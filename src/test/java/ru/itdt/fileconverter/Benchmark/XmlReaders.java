package ru.itdt.fileconverter.Benchmark;

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

/**
 * Проверка времени работы Sax и Jaxb для считывания файла.
 */
public class XmlReaders {

    @State(Scope.Thread)
    public static class MyState {
        private final String pathToFile = "src/test/resources/TestInput.xml";
        private final Reader<XmlRoot> saxReader = new SaxReader();
        private final Reader<XmlRoot> jaxbReader;

        {
            try {
                jaxbReader = new JaxbReader();
            } catch (JAXBException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void readSax(final MyState state,final Blackhole blackhole)
        throws JAXBException, ParserConfigurationException, IOException, SAXException {
        blackhole.consume(state.saxReader.parse(state.pathToFile));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void readJaxb(final MyState state,final Blackhole blackhole)
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
