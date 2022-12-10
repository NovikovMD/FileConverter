import fileconverter.bean.json.JsonUpperClass;
import fileconverter.bean.xml.*;
import fileconverter.converters.Converter;
import fileconverter.converters.XmlToJson;
import fileconverter.readers.JaxbReader;
import fileconverter.readers.Reader;
import fileconverter.readers.SaxReader;
import fileconverter.writers.JacksonWriter;
import fileconverter.writers.Writer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class XmlToJsonTest {
    Reader reader;
    Converter converter;
    Writer writer;

    @BeforeEach
    void starter() {
        reader = new JaxbReader();
        converter = new XmlToJson();
        writer = new JacksonWriter();
    }

    @Test
    void parseXml() throws ParserConfigurationException, IOException, SAXException, JAXBException {
        XmlUpperClass upper = (XmlUpperClass) reader.parse(
            new FileInputStream("src/test/resources/TestInput.xml"));
        //publisher
        assertEquals("Rockstar", upper.getPublishers()
            .get(0)
            .getName());

        //developer
        XmlDevStudio devStudio = upper.getPublishers()
            .get(0)
            .getDevStudios()
            .get(0);
        assertEquals("Rockstar Toronto", devStudio.getName());
        assertEquals(1981, devStudio.getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", devStudio.getUrl());

        //games
        XmlGame game = devStudio.getGames().get(0);
        assertEquals("The Warriors", game.getName());
        assertEquals(2005, game.getYear());

        game = devStudio.getGames().get(1);
        assertEquals("Manhunt 2", game.getName());
        assertEquals(2007, game.getYear());

        //platforms
        XmlPlatform platform = devStudio.getGames().get(0).getPlatforms().get(0);
        assertEquals("PlayStation 2", platform.getName());
        platform = devStudio.getGames().get(0).getPlatforms().get(1);
        assertEquals("PlayStation Portable", platform.getName());
        platform = devStudio.getGames().get(0).getPlatforms().get(2);
        assertEquals("XBox", platform.getName());

        platform = devStudio.getGames().get(1).getPlatforms().get(0);
        assertEquals("Microsoft Windows", platform.getName());
        platform = devStudio.getGames().get(1).getPlatforms().get(1);
        assertEquals("PlayStation 2", platform.getName());
        platform = devStudio.getGames().get(1).getPlatforms().get(2);
        assertEquals("PlayStation Portable", platform.getName());
        platform = devStudio.getGames().get(1).getPlatforms().get(3);
        assertEquals("Wii", platform.getName());
    }

    @Test
    void convertXmlToJson() throws ParserConfigurationException, IOException, SAXException, JAXBException {
        JsonUpperClass compare = (JsonUpperClass) converter.convert(
            reader.parse(
                new FileInputStream("src/test/resources/TestInput.xml")));

        assertEquals("The Warriors", compare.getGames().get(0).getName());
        assertEquals(2005, compare.getGames().get(0).getYear());
        assertEquals("Rockstar", compare.getGames().get(0).getGamePublisher());

        assertEquals("PlayStation 2", compare.getGames().get(0).getPlatforms().get(0).getName());
        assertEquals("PlayStation Portable", compare.getGames().get(0).getPlatforms().get(1).getName());
        assertEquals("XBox", compare.getGames().get(0).getPlatforms().get(2).getName());

        assertEquals("Rockstar Toronto", compare.getGames().get(0).getDevStudios().get(0).getName());
        assertEquals(1981, compare.getGames().get(0).getDevStudios().get(0).getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", compare.getGames().get(0).getDevStudios().get(0).getUrl());


        assertEquals("Manhunt 2", compare.getGames().get(1).getName());
        assertEquals(2007, compare.getGames().get(1).getYear());
        assertEquals("Rockstar", compare.getGames().get(1).getGamePublisher());

        assertEquals("Microsoft Windows", compare.getGames().get(1).getPlatforms().get(0).getName());
        assertEquals("PlayStation 2", compare.getGames().get(1).getPlatforms().get(1).getName());
        assertEquals("PlayStation Portable", compare.getGames().get(1).getPlatforms().get(2).getName());
        assertEquals("Wii", compare.getGames().get(1).getPlatforms().get(3).getName());

        assertEquals("Rockstar Toronto", compare.getGames().get(1).getDevStudios().get(0).getName());
        assertEquals(1981, compare.getGames().get(1).getDevStudios().get(0).getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", compare.getGames().get(1).getDevStudios().get(0).getUrl());
        assertEquals("Rockstar London", compare.getGames().get(1).getDevStudios().get(1).getName());
        assertEquals(2005, compare.getGames().get(1).getDevStudios().get(1).getYearOfFoundation());
        assertEquals("www.rockstarlondon.com", compare.getGames().get(1).getDevStudios().get(1).getUrl());
    }

    @Test
    void createJson() throws IOException, ParserConfigurationException, SAXException, XMLStreamException, JAXBException {
        File fl = new File("src/test/resources/newJson.json");
        if (fl.exists())
            fl.delete();

        writer.write(
            converter.convert(
                reader.parse(
                    new FileInputStream("src/test/resources/TestInput.xml"))),
            new FileOutputStream("src/test/resources/newJson.json"));

        assertTrue(new File("src/test/resources/newJson.json").exists());
    }

    @Test
    void wrongFile() {
        assertEquals("src\\test\\resources\\NoSuchFile.xml (Не удается найти указанный файл)",
            assertThrows(Exception.class,
                () -> reader.parse(
                    new FileInputStream("src/test/resources/NoSuchFile.xml")))
                .getMessage());
    }
}
