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

        assertEquals("The Warriors", compare.getGames()
            .get(0).getName());
        assertEquals(2005, compare.getGames()
            .get(0).getYear());
        assertEquals("Rockstar", compare.getGames()
            .get(0).getGamePublisher());

        assertEquals("PlayStation 2", compare.getGames()
            .get(0).getPlatforms()
            .get(0).getName());
        assertEquals("PlayStation Portable", compare.getGames()
            .get(0).getPlatforms()
            .get(1).getName());
        assertEquals("XBox", compare.getGames()
            .get(0).getPlatforms()
            .get(2).getName());

        assertEquals("Rockstar Toronto", compare.getGames()
            .get(0).getDevStudios()
            .get(0).getName());
        assertEquals(1981, compare.getGames()
            .get(0).getDevStudios()
            .get(0).getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", compare.getGames()
            .get(0).getDevStudios()
            .get(0).getUrl());

        assertEquals("Manhunt 2", compare.getGames()
            .get(1).getName());
        assertEquals(2007, compare.getGames()
            .get(1).getYear());
        assertEquals("Rockstar", compare.getGames()
            .get(1).getGamePublisher());

        assertEquals("Microsoft Windows", compare.getGames()
            .get(1).getPlatforms()
            .get(0).getName());
        assertEquals("PlayStation 2", compare.getGames()
            .get(1).getPlatforms()
            .get(1).getName());
        assertEquals("PlayStation Portable", compare.getGames()
            .get(1).getPlatforms()
            .get(2).getName());
        assertEquals("Wii", compare.getGames()
            .get(1).getPlatforms()
            .get(3).getName());

        assertEquals("Rockstar Toronto", compare.getGames()
            .get(1).getDevStudios()
            .get(0).getName());
        assertEquals(1981, compare.getGames()
            .get(1).getDevStudios()
            .get(0).getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", compare.getGames()
            .get(1).getDevStudios()
            .get(0).getUrl());
        assertEquals("Rockstar London", compare.getGames()
            .get(1).getDevStudios()
            .get(1).getName());
        assertEquals(2005, compare.getGames()
            .get(1).getDevStudios()
            .get(1).getYearOfFoundation());
        assertEquals("www.rockstarlondon.com", compare.getGames()
            .get(1).getDevStudios()
            .get(1).getUrl());
    }

    @Test
    void jsonToXml() throws IOException {
        val reader = new JacksonReader();
        val converter = new JsonToXml();

        final XmlRoot xmlRoot = converter.convert(
            reader.parse("src\\test\\resources\\TestInput.json"));

        assertEquals("Rockstar", xmlRoot.getPublishers().get(0).getName());

        assertEquals("Rockstar Toronto", xmlRoot.getPublishers()
            .get(0).getDevStudios()
            .get(0).getName());
        assertEquals(1981, xmlRoot.getPublishers()
            .get(0).getDevStudios()
            .get(0).getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", xmlRoot.getPublishers()
            .get(0).getDevStudios()
            .get(0).getUrl());

        assertEquals("The Warriors", xmlRoot.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getName());
        assertEquals(2005, xmlRoot.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getYear());

        assertEquals("PlayStation 2", xmlRoot.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getPlatforms()
            .get(0).getName());
        assertEquals("PlayStation Portable", xmlRoot.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getPlatforms()
            .get(1).getName());
        assertEquals("XBox", xmlRoot.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getPlatforms()
            .get(2).getName());
    }
}
