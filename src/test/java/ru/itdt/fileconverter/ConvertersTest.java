package ru.itdt.fileconverter;

import ru.itdt.fileconverter.bean.json.JsonRoot;
import ru.itdt.fileconverter.bean.xml.XmlRoot;
import ru.itdt.fileconverter.converters.JsonToXml;
import ru.itdt.fileconverter.converters.XmlToJson;
import ru.itdt.fileconverter.readers.json.JacksonReader;
import ru.itdt.fileconverter.readers.xml.SaxReader;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvertersTest {
    @Test
    void xmlToJson() throws ParserConfigurationException, IOException, SAXException {
        val reader = new SaxReader();
        val converter = new XmlToJson();
        final JsonRoot compare = converter.convert(
            reader.parse("src/test/resources/TestInput.xml"));

        theWarriors(compare.getGames()
                .get(0).getName(),
            compare.getGames()
                .get(0).getYear(),
            compare.getGames()
                .get(0).getGamePublisher());

        theWarriorsPlatforms(compare.getGames()
                .get(0).getPlatforms()
                .get(0).getName(),
            compare.getGames()
                .get(0).getPlatforms()
                .get(1).getName(),
            compare.getGames()
                .get(0).getPlatforms()
                .get(2).getName());

        rockstarToronto(compare.getGames()
                .get(1).getDevStudios()
                .get(0).getName(),
            compare.getGames()
                .get(1).getDevStudios()
                .get(0).getYearOfFoundation(),
            compare.getGames()
                .get(1).getDevStudios()
                .get(0).getUrl());

        manhunt(compare.getGames()
                .get(1).getName(),
            compare.getGames()
                .get(1).getYear(),
            compare.getGames()
                .get(1).getGamePublisher());

        manhuntPlatforms(compare.getGames()
                .get(1).getPlatforms()
                .get(0).getName(),
            compare.getGames()
                .get(1).getPlatforms()
                .get(1).getName(),
            compare.getGames()
                .get(1).getPlatforms()
                .get(2).getName(),
            compare.getGames()
                .get(1).getPlatforms()
                .get(3).getName());

        rockstarLondon(compare.getGames()
                .get(1).getDevStudios()
                .get(1).getName(),
            compare.getGames()
                .get(1).getDevStudios()
                .get(1).getYearOfFoundation(),
            compare.getGames()
                .get(1).getDevStudios()
                .get(1).getUrl());
    }

    private static void rockstarLondon(String name, int year, String url) {
        assertEquals("Rockstar London", name);
        assertEquals(2005, year);
        assertEquals("www.rockstarlondon.com", url);
    }

    private static void manhuntPlatforms(String platform1, String platform2,
                                         String platform3, String platform4) {
        assertEquals("Microsoft Windows", platform1);
        assertEquals("PlayStation 2", platform2);
        assertEquals("PlayStation Portable", platform3);
        assertEquals("Wii", platform4);
    }

    private static void manhunt(String name, int year, String publisher) {
        assertEquals("Manhunt 2", name);
        assertEquals(2007, year);
        assertEquals("Rockstar", publisher);
    }

    private static void theWarriorsPlatforms(String platform1, String platform2, String platform3) {
        assertEquals("PlayStation 2", platform1);
        assertEquals("PlayStation Portable", platform2);
        assertEquals("XBox", platform3);
    }

    private static void theWarriors(String name, int year, String publisher) {
        assertEquals("The Warriors", name);
        assertEquals(2005, year);
        assertEquals("Rockstar", publisher);
    }

    private static void rockstarToronto(String devStudio, int year, String url) {
        assertEquals("Rockstar Toronto", devStudio);
        assertEquals(1981, year);
        assertEquals("www.rockstartoronto.com", url);
    }

    @Test
    void jsonToXml() throws IOException {
        val reader = new JacksonReader();
        val converter = new JsonToXml();

        final XmlRoot xmlRoot = converter.convert(
            reader.parse("src\\test\\resources\\TestInput.json"));

        rockstarToronto(xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getName(),
            xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getYearOfFoundation(),
            xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getUrl());

        theWarriors(xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getGames()
                .get(0).getName(),
            xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getGames()
                .get(0).getYear(),
            xmlRoot.getPublishers().get(0).getName());

        theWarriorsPlatforms(xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getGames()
                .get(0).getPlatforms()
                .get(0).getName(),
            xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getGames()
                .get(0).getPlatforms()
                .get(1).getName(),
            xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getGames()
                .get(0).getPlatforms()
                .get(2).getName());

        manhunt(xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getGames()
                .get(1).getName(),
            xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getGames()
                .get(1).getYear(),
            xmlRoot.getPublishers().get(0).getName());

        manhuntPlatforms(xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getGames()
                .get(1).getPlatforms()
                .get(0).getName(),
            xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getGames()
                .get(1).getPlatforms()
                .get(1).getName(),
            xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getGames()
                .get(1).getPlatforms()
                .get(2).getName(),
            xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(0).getGames()
                .get(1).getPlatforms()
                .get(3).getName());

        rockstarLondon(xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(1).getName(),
            xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(1).getYearOfFoundation(),
            xmlRoot.getPublishers()
                .get(0).getDevStudios()
                .get(1).getUrl());
    }
}
