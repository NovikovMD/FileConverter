package ru.itdt.fileconverter.Benchmark;

import lombok.val;
import org.xml.sax.SAXException;
import ru.itdt.fileconverter.FileConverter;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.time.LocalTime;

import static ru.itdt.fileconverter.bean.InputParamValidator.createBean;

public class FullApplication {
    public static void main(String[] args)
        throws IOException, XMLStreamException, JAXBException, ParserConfigurationException, SAXException {
        val fileConverter = new FileConverter();
        val inputs = createBean(new String[]{"src/test/resources/TestInput.json",
            "src/test/resources/TestMain.xml"});

        //Разминка
        for (int i = 0; i < 500; i++) {
            fileConverter.doParse(inputs);
        }

        val n = 10000;
        val timeStart = LocalTime.now();
        for (int i = 0; i < n; i++) {
            fileConverter.doParse(inputs);
        }
        val timeEnd = LocalTime.now();

        long timeCost = convertToSec(timeEnd) - convertToSec(timeStart);
        String notation = "с";//секунды
        if (timeCost == 0){
            timeCost = convertToNano(timeEnd) - convertToNano(timeStart);
            notation = "нс";//наносекунды
        }

        System.out.printf("Время на выполнение %d операций - %d %s.%n",
            n, timeCost, notation);
        System.out.printf("Среднее время на выполнение 1 операции - %d нс.%n",
            (convertToNano(timeEnd) - convertToNano(timeStart))/n);
    }

    public static long convertToNano(final LocalTime time){
        return time.getHour() * 3600000000000L +
            time.getMinute() * 60000000000L +
            time.getSecond() * 1000000000L +
            time.getNano();
    }
    public static long convertToSec(final LocalTime time){
        return time.getHour() * 3600L +
            time.getMinute() * 60L +
            time.getSecond();
    }
}
