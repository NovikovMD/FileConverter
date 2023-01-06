import ru.itdt.fileconverter.bean.xml.XmlRoot;
import ru.itdt.fileconverter.converters.JsonToXml;
import ru.itdt.fileconverter.converters.XmlToJson;
import ru.itdt.fileconverter.readers.json.GsonReader;
import ru.itdt.fileconverter.readers.json.JacksonReader;
import ru.itdt.fileconverter.readers.xml.JaxbReader;
import ru.itdt.fileconverter.readers.xml.SaxReader;
import ru.itdt.fileconverter.writers.json.GsonWriter;
import ru.itdt.fileconverter.writers.json.JacksonWriter;
import ru.itdt.fileconverter.writers.xml.JaxbWriter;
import ru.itdt.fileconverter.writers.xml.StaxWriter;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WritersTest {
    @Test
    void writeGson() throws ParserConfigurationException, IOException, SAXException {
        val reader = new SaxReader();
        val converter = new XmlToJson();
        val writer = new GsonWriter();

        val fl = new File("src/test/resources/newJson.json");
        if (fl.exists())
            fl.delete();

        writer.write(
            converter.convert(
                reader.parse("src/test/resources/TestInput.xml")),
            "src/test/resources/newJson.json");

        assertTrue(new File("src/test/resources/newJson.json").exists());
        compareCreatedJsonFile();
    }

    @Test
    void writeJackSon() throws ParserConfigurationException, IOException, SAXException {
        val reader = new SaxReader();
        val converter = new XmlToJson();
        val writer = new JacksonWriter();

        val fl = new File("src/test/resources/newJson.json");
        if (fl.exists())
            fl.delete();

        writer.write(
            converter.convert(
                reader.parse("src/test/resources/TestInput.xml")),
            "src/test/resources/newJson.json");

        assertTrue(new File("src/test/resources/newJson.json").exists());
        compareCreatedJsonFile();
    }

    private static void compareCreatedJsonFile() throws IOException {
        val compareReader = new JacksonReader();
        val upper = compareReader.parse("src/test/resources/newJson.json");

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

    @Test
    void writeJaxb() throws JAXBException, IOException {
        val reader = new GsonReader();
        val converter = new JsonToXml();
        val writer = new JaxbWriter();

        val fl = new File("src/test/resources/NewXML.xml");
        if (fl.exists())
            fl.delete();

        writer.write(
            converter.convert(
                reader.parse("src/test/resources/TestInput.json")),
            "src/test/resources/NewXML.xml");

        assertTrue(new File("src/test/resources/newJson.json").exists());

        compareCreatedXmlFile();
    }

    @Test
    void writeStax() throws IOException, JAXBException, XMLStreamException {
        val reader = new GsonReader();
        val converter = new JsonToXml();
        val writer = new StaxWriter();

        val fl = new File("src/test/resources/NewXML.xml");
        if (fl.exists())
            fl.delete();

        writer.write(
            converter.convert(
                reader.parse("src/test/resources/TestInput.json")),
            "src/test/resources/NewXML.xml");

        assertTrue(new File("src/test/resources/newJson.json").exists());

        compareCreatedXmlFile();
    }

    private static void compareCreatedXmlFile() throws JAXBException, IOException {
        val compareReader = new JaxbReader();
        final XmlRoot upper = compareReader.parse("src/test/resources/TestInput.xml");

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
}
