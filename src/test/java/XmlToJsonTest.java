import fileconverter.bean.json.JsonUpperClass;
import fileconverter.bean.xml.*;
import fileconverter.xmltojson.XmlToJson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


public class XmlToJsonTest {

    private static final XmlToJson XML_TO_JSON_PARSER = new XmlToJson();

    @Test
    public void parseXml() throws ParserConfigurationException, IOException, SAXException {
        //publisher
        XmlGamePublisher publisher = XML_TO_JSON_PARSER.parseXml("src/test/resources/TestInput.xml")
            .getPublishers().get(0);
        assertEquals("Rockstar", publisher.getName());

        //developer
        XmlDevStudio devStudio = publisher.getDevStudios().get(0);
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
    public void convertXmlToJson() throws ParserConfigurationException, IOException, SAXException {
        JsonUpperClass compare = XML_TO_JSON_PARSER.convert(
            XML_TO_JSON_PARSER.parseXml("src/test/resources/TestInput.xml"));

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
    public void createJson() throws IOException, ParserConfigurationException, SAXException {
        XML_TO_JSON_PARSER.createJson(
            XML_TO_JSON_PARSER.convert(
                XML_TO_JSON_PARSER.parseXml("src/test/resources/TestInput.xml")),
            "src/test/resources/newName.json");

        assertTrue(new File("src/test/resources/newName.json").exists());
    }

    @Test
    public void wrongFile() {
        assertEquals("Неверный путь к файлу.",
            assertThrows(Exception.class,
                () -> XML_TO_JSON_PARSER.parseXml("src/test/resources/NoSuchFile.xml"))
                .getMessage());
    }
}
