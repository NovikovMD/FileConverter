import fileconverter.bean.json.JsonUpper;
import fileconverter.bean.xml.XmlUpper;
import fileconverter.converters.XmlToJson;
import fileconverter.readers.Reader;
import fileconverter.readers.xml.JaxbReader;
import fileconverter.readers.xml.SaxReader;
import fileconverter.writers.Writer;
import fileconverter.writers.json.GsonWriter;
import fileconverter.writers.json.JacksonWriter;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class XmlToJsonTest {
    private static Stream<Arguments> setConfig() throws JAXBException {
        return Stream.of(
            Arguments.of(new JaxbReader(), new XmlToJson(), new GsonWriter()),
            Arguments.of(new SaxReader(), new XmlToJson(), new JacksonWriter())
        );
    }

    @ParameterizedTest
    @MethodSource("setConfig")
    void parseXml(final Reader<XmlUpper> reader)
        throws ParserConfigurationException, IOException,
        SAXException, JAXBException {
        final XmlUpper upper = reader.parse(
            new FileInputStream("src/test/resources/TestInput.xml"));

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

    @ParameterizedTest
    @MethodSource("setConfig")
    void convertXmlToJson(final Reader<XmlUpper> reader, final XmlToJson converter)
        throws ParserConfigurationException, IOException,
        SAXException, JAXBException {
        final JsonUpper compare = converter.convert(
            reader.parse(
                new FileInputStream("src/test/resources/TestInput.xml")));

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

    @ParameterizedTest
    @MethodSource("setConfig")
    void createJson(final Reader<XmlUpper> reader, final XmlToJson converter, final Writer<JsonUpper> writer)
        throws IOException, ParserConfigurationException, SAXException,
        XMLStreamException, JAXBException {
        val fl = new File("src/test/resources/newJson.json");
        if (fl.exists())
            fl.delete();

        writer.write(
            converter.convert(
                reader.parse(
                    new FileInputStream("src/test/resources/TestInput.xml"))),
            new FileOutputStream("src/test/resources/newJson.json"));

        assertTrue(new File("src/test/resources/newJson.json").exists());
    }

    @ParameterizedTest
    @MethodSource("setConfig")
    void wrongFile(final Reader<XmlUpper> reader) {
        assertEquals("src\\test\\resources\\NoSuchFile.xml (Не удается найти указанный файл)",
            assertThrows(Exception.class,
                () -> reader.parse(
                    new FileInputStream("src/test/resources/NoSuchFile.xml")))
                .getMessage());
    }
}
