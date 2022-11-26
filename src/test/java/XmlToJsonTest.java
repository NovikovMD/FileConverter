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
    public void tryParse() throws ParserConfigurationException, IOException, SAXException {
        XmlUpperClass publishers = xmlToJsonParser.parseXml("src/test/resources/TestInput.xml");
        //try parse multiple times
        publishers = xmlToJsonParser.parseXml("src/test/resources/TestInput.xml");
        publishers = xmlToJsonParser.parseXml("src/test/resources/TestInput.xml");
        publishers = xmlToJsonParser.parseXml("src/test/resources/TestInput.xml");

        //publisher
        XmlGamePublisher publisher = publishers.getPublishers().get(0);
        Assert.assertEquals(publisher.getName(), "Rockstar");

        //developer
        XmlDevStudio devStudio = publisher.getDevStudios().get(0);
        Assert.assertEquals(devStudio.getName(), "Rockstar Toronto");

        Assert.assertEquals(devStudio.getYearOfFoundation(), 1981);

        Assert.assertEquals(devStudio.getUrl(), "www.rockstartoronto.com");

        //games
        XmlGame game = devStudio.getGames().get(0);
        Assert.assertEquals(game.getName(), "The Warriors");
        Assert.assertEquals(game.getYear(), 2005);

        game = devStudio.getGames().get(1);
        Assert.assertEquals(game.getName(), "Manhunt 2");
        Assert.assertEquals(game.getYear(), 2007);

        //platforms
        XmlPlatform platform = devStudio.getGames().get(0).getPlatforms().get(0);
        Assert.assertEquals(platform.getName(), "PlayStation 2");
        platform = devStudio.getGames().get(0).getPlatforms().get(1);
        Assert.assertEquals(platform.getName(), "PlayStation Portable");
        platform = devStudio.getGames().get(0).getPlatforms().get(2);
        Assert.assertEquals(platform.getName(), "XBox");

        platform = devStudio.getGames().get(1).getPlatforms().get(0);
        Assert.assertEquals(platform.getName(), "Microsoft Windows");
        platform = devStudio.getGames().get(1).getPlatforms().get(1);
        Assert.assertEquals(platform.getName(), "PlayStation 2");
        platform = devStudio.getGames().get(1).getPlatforms().get(2);
        Assert.assertEquals(platform.getName(), "PlayStation Portable");
        platform = devStudio.getGames().get(1).getPlatforms().get(3);
        Assert.assertEquals(platform.getName(), "Wii");
    }

    @Test
    public void tryConvertXmlToJson() throws ParserConfigurationException, IOException, SAXException {
        XmlUpperClass xmlClass = xmlToJsonParser.parseXml("src/test/resources/TestInput.xml");

        JsonUpperClass compare = xmlToJsonParser.convert(xmlClass);

        Assert.assertEquals(compare.getGames().get(0).getName(), "The Warriors");
        Assert.assertEquals(compare.getGames().get(0).getYear(), 2005);
        Assert.assertEquals(compare.getGames().get(0).getGamePublisher(), "Rockstar");

        Assert.assertEquals(compare.getGames().get(0).getPlatforms().get(0).getName(), "PlayStation 2");
        Assert.assertEquals(compare.getGames().get(0).getPlatforms().get(1).getName(), "PlayStation Portable");
        Assert.assertEquals(compare.getGames().get(0).getPlatforms().get(2).getName(), "XBox");

        Assert.assertEquals(compare.getGames().get(0).getDevStudios().get(0).getName(), "Rockstar Toronto");
        Assert.assertEquals(compare.getGames().get(0).getDevStudios().get(0).getYearOfFoundation(), 1981);
        Assert.assertEquals(compare.getGames().get(0).getDevStudios().get(0).getUrl(), "www.rockstartoronto.com");


        Assert.assertEquals(compare.getGames().get(1).getName(), "Manhunt 2");
        Assert.assertEquals(compare.getGames().get(1).getYear(), 2007);
        Assert.assertEquals(compare.getGames().get(1).getGamePublisher(), "Rockstar");

        Assert.assertEquals(compare.getGames().get(1).getPlatforms().get(0).getName(), "Microsoft Windows");
        Assert.assertEquals(compare.getGames().get(1).getPlatforms().get(1).getName(), "PlayStation 2");
        Assert.assertEquals(compare.getGames().get(1).getPlatforms().get(2).getName(), "PlayStation Portable");
        Assert.assertEquals(compare.getGames().get(1).getPlatforms().get(3).getName(), "Wii");

        Assert.assertEquals(compare.getGames().get(1).getDevStudios().get(0).getName(), "Rockstar Toronto");
        Assert.assertEquals(compare.getGames().get(1).getDevStudios().get(0).getYearOfFoundation(), 1981);
        Assert.assertEquals(compare.getGames().get(1).getDevStudios().get(0).getUrl(), "www.rockstartoronto.com");
        Assert.assertEquals(compare.getGames().get(1).getDevStudios().get(1).getName(), "Rockstar London");
        Assert.assertEquals(compare.getGames().get(1).getDevStudios().get(1).getYearOfFoundation(), 2005);
        Assert.assertEquals(compare.getGames().get(1).getDevStudios().get(1).getUrl(), "www.rockstarlondon.com");
    }

    @Test
    public void tryCreateJsonFile() throws IOException, ParserConfigurationException, SAXException {
        XmlUpperClass xmlClass =xmlToJsonParser.parseXml("src/test/resources/TestInput.xml");
        JsonUpperClass converted = xmlToJsonParser.convert(xmlClass);
        xmlToJsonParser.createJson(converted, "src/test/resources/newName.json");

        File fl = new File("src/test/resources/newName.json");

        Assert.assertTrue(fl.exists());
    }
}
