import fileconverter.bean.json.JsonUpperClass;
import fileconverter.bean.xml.*;
import fileconverter.xmltojson.XmlToJson;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertThrows;

public class XmlToJsonTest {

    private static final XmlToJson XML_TO_JSON_PARSER = new XmlToJson();

    @Test
    public void parseXml() throws ParserConfigurationException, IOException, SAXException {
        XmlUpperClass publishers = XML_TO_JSON_PARSER.parseXml("src/test/resources/TestInput.xml");

        //publisher
        XmlGamePublisher publisher = publishers.getPublishers().get(0);
        Assert.assertEquals("Rockstar", publisher.getName());

        //developer
        XmlDevStudio devStudio = publisher.getDevStudios().get(0);
        Assert.assertEquals("Rockstar Toronto", devStudio.getName());

        Assert.assertEquals(1981, devStudio.getYearOfFoundation());

        Assert.assertEquals("www.rockstartoronto.com", devStudio.getUrl());

        //games
        XmlGame game = devStudio.getGames().get(0);
        Assert.assertEquals("The Warriors", game.getName());
        Assert.assertEquals(2005, game.getYear());

        game = devStudio.getGames().get(1);
        Assert.assertEquals("Manhunt 2", game.getName());
        Assert.assertEquals(2007, game.getYear());

        //platforms
        XmlPlatform platform = devStudio.getGames().get(0).getPlatforms().get(0);
        Assert.assertEquals("PlayStation 2", platform.getName());
        platform = devStudio.getGames().get(0).getPlatforms().get(1);
        Assert.assertEquals("PlayStation Portable", platform.getName());
        platform = devStudio.getGames().get(0).getPlatforms().get(2);
        Assert.assertEquals("XBox", platform.getName());

        platform = devStudio.getGames().get(1).getPlatforms().get(0);
        Assert.assertEquals("Microsoft Windows", platform.getName());
        platform = devStudio.getGames().get(1).getPlatforms().get(1);
        Assert.assertEquals("PlayStation 2", platform.getName());
        platform = devStudio.getGames().get(1).getPlatforms().get(2);
        Assert.assertEquals("PlayStation Portable", platform.getName());
        platform = devStudio.getGames().get(1).getPlatforms().get(3);
        Assert.assertEquals("Wii", platform.getName());
    }

    @Test
    public void convertXmlToJson() throws ParserConfigurationException, IOException, SAXException {
        XmlUpperClass xmlClass = XML_TO_JSON_PARSER.parseXml("src/test/resources/TestInput.xml");

        JsonUpperClass compare = XML_TO_JSON_PARSER.convert(xmlClass);

        Assert.assertEquals("The Warriors", compare.getGames().get(0).getName());
        Assert.assertEquals(2005, compare.getGames().get(0).getYear());
        Assert.assertEquals("Rockstar", compare.getGames().get(0).getGamePublisher());

        Assert.assertEquals("PlayStation 2", compare.getGames().get(0).getPlatforms().get(0).getName());
        Assert.assertEquals("PlayStation Portable", compare.getGames().get(0).getPlatforms().get(1).getName());
        Assert.assertEquals("XBox", compare.getGames().get(0).getPlatforms().get(2).getName());

        Assert.assertEquals("Rockstar Toronto", compare.getGames().get(0).getDevStudios().get(0).getName());
        Assert.assertEquals(1981, compare.getGames().get(0).getDevStudios().get(0).getYearOfFoundation());
        Assert.assertEquals("www.rockstartoronto.com", compare.getGames().get(0).getDevStudios().get(0).getUrl());


        Assert.assertEquals("Manhunt 2", compare.getGames().get(1).getName());
        Assert.assertEquals(2007, compare.getGames().get(1).getYear());
        Assert.assertEquals("Rockstar", compare.getGames().get(1).getGamePublisher());

        Assert.assertEquals("Microsoft Windows", compare.getGames().get(1).getPlatforms().get(0).getName());
        Assert.assertEquals("PlayStation 2", compare.getGames().get(1).getPlatforms().get(1).getName());
        Assert.assertEquals("PlayStation Portable", compare.getGames().get(1).getPlatforms().get(2).getName());
        Assert.assertEquals("Wii", compare.getGames().get(1).getPlatforms().get(3).getName());

        Assert.assertEquals("Rockstar Toronto", compare.getGames().get(1).getDevStudios().get(0).getName());
        Assert.assertEquals(1981, compare.getGames().get(1).getDevStudios().get(0).getYearOfFoundation());
        Assert.assertEquals("www.rockstartoronto.com", compare.getGames().get(1).getDevStudios().get(0).getUrl());
        Assert.assertEquals("Rockstar London", compare.getGames().get(1).getDevStudios().get(1).getName());
        Assert.assertEquals(2005, compare.getGames().get(1).getDevStudios().get(1).getYearOfFoundation());
        Assert.assertEquals("www.rockstarlondon.com", compare.getGames().get(1).getDevStudios().get(1).getUrl());
    }

    @Test
    public void createJson() throws IOException, ParserConfigurationException, SAXException {
        XmlUpperClass xmlClass = XML_TO_JSON_PARSER.parseXml("src/test/resources/TestInput.xml");
        JsonUpperClass converted = XML_TO_JSON_PARSER.convert(xmlClass);
        XML_TO_JSON_PARSER.createJson(converted, "src/test/resources/newName.json");

        File fl = new File("src/test/resources/newName.json");

        Assert.assertTrue(fl.exists());
    }

    @Test
    public void wrongFile() {
        Exception exception = assertThrows(Exception.class,
            () -> XML_TO_JSON_PARSER.parseXml("src/test/resources/NoSuchFile.xml"));

        Assert.assertEquals("Неверный путь к файлу.", exception.getMessage());
    }
}
