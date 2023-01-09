package ru.itdt.fileconverter;

import ru.itdt.fileconverter.bean.json.JsonRoot;
import ru.itdt.fileconverter.bean.xml.XmlRoot;
import ru.itdt.fileconverter.readers.Reader;
import ru.itdt.fileconverter.readers.json.GsonReader;
import ru.itdt.fileconverter.readers.xml.JaxbReader;
import ru.itdt.fileconverter.readers.xml.SaxReader;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReadersTest {
    @Test
    void readSAX() throws ParserConfigurationException, IOException, SAXException, JAXBException {
        val reader = new SaxReader();
        compareParsedXmlFile(reader);
    }

    @Test
    void readJaxb() throws JAXBException, IOException, ParserConfigurationException, SAXException {
        val reader = new JaxbReader();
        compareParsedXmlFile(reader);
    }

    private static void compareParsedXmlFile(final Reader<XmlRoot> reader)
        throws JAXBException, IOException, ParserConfigurationException, SAXException {
        final XmlRoot upper = reader.parse("src/test/resources/TestInput.xml");

        assertEquals("Rockstar", upper.getPublishers()
            .get(0).getName());

        assertEquals("Rockstar Toronto", upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getName());
        assertEquals(1981, upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getUrl());

        assertEquals("The Warriors", upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getName());
        assertEquals(2005, upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getYear());

        assertEquals("Manhunt 2", upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(1).getName());
        assertEquals(2007, upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(1).getYear());

        assertEquals("PlayStation 2", upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getPlatforms()
            .get(0).getName());
        assertEquals("PlayStation Portable", upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getPlatforms()
            .get(1).getName());
        assertEquals("XBox", upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getPlatforms()
            .get(2).getName());

        assertEquals("Microsoft Windows", upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(1).getPlatforms()
            .get(0).getName());
        assertEquals("PlayStation 2", upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(1).getPlatforms()
            .get(1).getName());
        assertEquals("PlayStation Portable", upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(1).getPlatforms()
            .get(2).getName());
        assertEquals("Wii", upper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(1).getPlatforms()
            .get(3).getName());
    }

    @Test
    void readJackson() throws JAXBException, IOException, ParserConfigurationException, SAXException {
        val reader = new GsonReader();
        compareParsedJsonFile(reader);
    }

    @Test
    void readGson() throws IOException, JAXBException, ParserConfigurationException, SAXException {
        val reader = new GsonReader();
        compareParsedJsonFile(reader);
    }

    private static void compareParsedJsonFile(final Reader<JsonRoot> reader)
        throws IOException, JAXBException, ParserConfigurationException, SAXException {
        final JsonRoot upper = reader.parse("src\\test\\resources\\TestInput.json");

        assertEquals("The Warriors", upper.getGames()
            .get(0).getName());
        assertEquals(2005, upper.getGames()
            .get(0).getYear());
        assertEquals("Rockstar", upper.getGames()
            .get(0).getGamePublisher());
        assertEquals("PlayStation 2", upper.getGames()
            .get(0).getPlatforms()
            .get(0).getName());
        assertEquals("PlayStation Portable", upper.getGames()
            .get(0).getPlatforms()
            .get(1).getName());
        assertEquals("XBox", upper.getGames()
            .get(0).getPlatforms()
            .get(2).getName());

        assertEquals("Rockstar Toronto", upper.getGames()
            .get(0).getDevStudios()
            .get(0).getName());
        assertEquals(1981, upper.getGames()
            .get(0).getDevStudios()
            .get(0).getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", upper.getGames()
            .get(0).getDevStudios()
            .get(0).getUrl());

        assertEquals("Manhunt 2", upper.getGames()
            .get(1).getName());
        assertEquals(2007, upper.getGames()
            .get(1).getYear());
        assertEquals("Rockstar", upper.getGames()
            .get(1).getGamePublisher());

        assertEquals("Microsoft Windows", upper.getGames()
            .get(1).getPlatforms()
            .get(0).getName());
        assertEquals("PlayStation 2", upper.getGames()
            .get(1).getPlatforms()
            .get(1).getName());
        assertEquals("PlayStation Portable", upper.getGames()
            .get(1).getPlatforms()
            .get(2).getName());
        assertEquals("Wii", upper.getGames()
            .get(1).getPlatforms()
            .get(3).getName());

        assertEquals("Rockstar Toronto", upper.getGames()
            .get(1).getDevStudios()
            .get(0).getName());
        assertEquals(1981, upper.getGames()
            .get(1).getDevStudios()
            .get(0).getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", upper.getGames()
            .get(1).getDevStudios()
            .get(0).getUrl());
    }
}
