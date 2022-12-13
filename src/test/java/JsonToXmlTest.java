import fileconverter.bean.json.JsonUpper;
import fileconverter.bean.xml.XmlUpper;
import fileconverter.converters.Converter;
import fileconverter.converters.JsonToXml;
import fileconverter.readers.Reader;
import fileconverter.readers.json.GsonReader;
import fileconverter.readers.json.JacksonReader;
import fileconverter.writers.Writer;
import fileconverter.writers.xml.JaxbWriter;
import fileconverter.writers.xml.StaxWriter;
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

public class JsonToXmlTest {
    private static Stream<Arguments> setConfig() throws JAXBException {
        return Stream.of(
            Arguments.of(new GsonReader(), new JsonToXml(), new JaxbWriter()),
            Arguments.of(new JacksonReader(), new JsonToXml(), new StaxWriter())
        );
    }

    @ParameterizedTest
    @MethodSource("setConfig")
    void tryParseJson(final Reader reader)
        throws IOException, ParserConfigurationException, SAXException, JAXBException {
        final JsonUpper upper = (JsonUpper) reader.parse(
            new FileInputStream("src\\test\\resources\\TestInput.json"));

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

    @ParameterizedTest
    @MethodSource("setConfig")
    void tryConvertXmlToJson(final Reader reader, final Converter converter)
        throws IOException, ParserConfigurationException, SAXException, JAXBException {
        final XmlUpper xmlUpper = (XmlUpper) converter.convert(
            reader.parse(
                new FileInputStream("src\\test\\resources\\TestInput.json")));

        assertEquals("Rockstar", xmlUpper.getPublishers().get(0).getName());

        assertEquals("Rockstar Toronto", xmlUpper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getName());
        assertEquals(1981, xmlUpper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", xmlUpper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getUrl());

        assertEquals("The Warriors", xmlUpper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getName());
        assertEquals(2005, xmlUpper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getYear());

        assertEquals("PlayStation 2", xmlUpper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getPlatforms()
            .get(0).getName());
        assertEquals("PlayStation Portable", xmlUpper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getPlatforms()
            .get(1).getName());
        assertEquals("XBox", xmlUpper.getPublishers()
            .get(0).getDevStudios()
            .get(0).getGames()
            .get(0).getPlatforms()
            .get(2).getName());
    }

    @ParameterizedTest
    @MethodSource("setConfig")
    void tryCreateXml(final Reader reader, final Converter converter, final Writer writer)
        throws IOException, ParserConfigurationException, SAXException, XMLStreamException, JAXBException {
        val fl = new File("src/test/resources/NewXML.xml");
        if (fl.exists())
            fl.delete();

        writer.write(
            converter.convert(
                reader.parse(
                    new FileInputStream("src\\test\\resources\\TestInput.json"))),
            new FileOutputStream("src/test/resources/NewXML.xml"));

        assertTrue(new File("src/test/resources/NewXML.xml").exists());
    }

    @ParameterizedTest
    @MethodSource("setConfig")
    void wrongFile(final Reader reader) {
        assertEquals("src\\test\\resources\\NoSuchFile.json (Не удается найти указанный файл)",
            assertThrows(Exception.class,
                () -> reader.parse(
                    new FileInputStream("src\\test\\resources\\NoSuchFile.json")))
                .getMessage());
    }
}
