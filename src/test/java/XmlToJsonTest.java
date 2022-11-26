import file_converter.classes.json.JsonUpperClass;
import file_converter.classes.xml.*;
import file_converter.xml_to_json.XmlToJson;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XmlToJsonTest {

    private static final XmlToJson xmlToJsonParser = new XmlToJson();

    @Test
    public void tryParseXml() throws ParserConfigurationException, IOException, SAXException {
        XmlUpperClass publishers = xmlToJsonParser.parseXml("src/test/resources/TestInput.xml");
        //try parse multiple times
        publishers = xmlToJsonParser.parseXml("src/test/resources/TestInput.xml");
        publishers = xmlToJsonParser.parseXml("src/test/resources/TestInput.xml");
        publishers = xmlToJsonParser.parseXml("src/test/resources/TestInput.xml");

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
    public void tryConvertXmlToJson() throws ParserConfigurationException, IOException, SAXException {
        XmlUpperClass xmlClass = xmlToJsonParser.parseXml("src/test/resources/TestInput.xml");

        JsonUpperClass compare = xmlToJsonParser.convert(xmlClass);

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
    public void tryCreateJson() throws IOException, ParserConfigurationException, SAXException {
        XmlUpperClass xmlClass = xmlToJsonParser.parseXml("src/test/resources/TestInput.xml");
        JsonUpperClass converted = xmlToJsonParser.convert(xmlClass);
        xmlToJsonParser.createJson(converted, "src/test/resources/newName.json");

        File fl = new File("src/test/resources/newName.json");

        Assert.assertTrue(fl.exists());
    }
}
