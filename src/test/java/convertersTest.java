import fileconverter.bean.json.JsonUpper;
import fileconverter.bean.xml.XmlUpper;
import fileconverter.converters.JsonToXml;
import fileconverter.converters.XmlToJson;
import fileconverter.readers.json.JacksonReader;
import fileconverter.readers.xml.SaxReader;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class convertersTest {
    @Test
    void xmlToJson() throws ParserConfigurationException, IOException, SAXException {
        val reader = new SaxReader();
        val converter = new XmlToJson();
        final JsonUpper compare = converter.convert(
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

        final XmlUpper xmlUpper = converter.convert(
            reader.parse("src\\test\\resources\\TestInput.json"));

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
}
