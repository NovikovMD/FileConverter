import fileconverter.bean.json.JsonUpperClass;
import fileconverter.bean.xml.*;

import fileconverter.converters.Converter;
import fileconverter.converters.JsonToXml;
import fileconverter.readers.JacksonReader;
import fileconverter.readers.Reader;
import fileconverter.writers.StaxWriter;
import fileconverter.writers.Writer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JsonToXmlTest {

    Reader reader;
    Converter converter;
    Writer writer;

    @BeforeEach
    void starter() {
        reader = new JacksonReader();
        converter = new JsonToXml();
        writer = new StaxWriter();
    }

    @Test
    void tryParseJson() throws IOException, ParserConfigurationException, SAXException {
        JsonUpperClass upper = (JsonUpperClass) reader.parse(
            new FileInputStream("src\\test\\resources\\TestInput.json"));


        assertEquals("The Warriors", upper.getGames().get(0).getName());
        assertEquals(2005, upper.getGames().get(0).getYear());
        assertEquals("Rockstar", upper.getGames().get(0).getGamePublisher());
        assertEquals("PlayStation 2", upper.getGames().get(0).getPlatforms().get(0).getName());
        assertEquals("PlayStation Portable", upper.getGames().get(0).getPlatforms().get(1).getName());
        assertEquals("XBox", upper.getGames().get(0).getPlatforms().get(2).getName());

        assertEquals("Rockstar Toronto", upper.getGames().get(0).getDevStudios().get(0).getName());
        assertEquals(1981, upper.getGames().get(0).getDevStudios().get(0).getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", upper.getGames().get(0).getDevStudios().get(0).getUrl());

        assertEquals("Manhunt 2", upper.getGames().get(1).getName());
        assertEquals(2007, upper.getGames().get(1).getYear());
        assertEquals("Rockstar", upper.getGames().get(1).getGamePublisher());

        assertEquals("Microsoft Windows", upper.getGames().get(1).getPlatforms().get(0).getName());
        assertEquals("PlayStation 2", upper.getGames().get(1).getPlatforms().get(1).getName());
        assertEquals("PlayStation Portable", upper.getGames().get(1).getPlatforms().get(2).getName());
        assertEquals("Wii", upper.getGames().get(1).getPlatforms().get(3).getName());

        assertEquals("Rockstar Toronto", upper.getGames().get(1).getDevStudios().get(0).getName());
        assertEquals(1981, upper.getGames().get(1).getDevStudios().get(0).getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", upper.getGames().get(1).getDevStudios().get(0).getUrl());

    }

    @Test
    void tryConvertXmlToJson() throws IOException, ParserConfigurationException, SAXException {
        XmlUpperClass xmlUpper = (XmlUpperClass) converter.convert(
            reader.parse(
                new FileInputStream("src\\test\\resources\\TestInput.json")));


        assertEquals("Rockstar", xmlUpper.getPublishers().get(0).getName());

        XmlDevStudio dev = xmlUpper.getPublishers().get(0).getDevStudios().get(0);
        assertEquals("Rockstar Toronto", dev.getName());
        assertEquals(1981, dev.getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", dev.getUrl());

        XmlGame game = dev.getGames().get(0);
        assertEquals("The Warriors", game.getName());
        assertEquals(2005, game.getYear());

        ArrayList<XmlPlatform> platforms = game.getPlatforms();
        assertEquals("PlayStation 2", platforms.get(0).getName());
        assertEquals("PlayStation Portable", platforms.get(1).getName());
        assertEquals("XBox", platforms.get(2).getName());
    }

    @Test
    void tryCreateXml() throws IOException, ParserConfigurationException, SAXException, XMLStreamException {
        writer.write(
            converter.convert(
                reader.parse(
                    new FileInputStream("src\\test\\resources\\TestInput.json"))),
            new FileOutputStream("src/test/resources/NewXML.xml"));

        assertTrue(new File("src/test/resources/NewXML.xml").exists());
    }

    @Test
    void wrongFile() {
        assertEquals("src\\test\\resources\\NoSuchFile.json (Не удается найти указанный файл)",
            assertThrows(Exception.class,
                () -> writer.write(
                    converter.convert(
                        reader.parse(
                            new FileInputStream("src\\test\\resources\\NoSuchFile.json"))),
                    new FileOutputStream("src/test/resources/NewXML.xml")))
                .getMessage());
    }
}
