import FileConverter.Classes.XML.*;
import FileConverter.XML_to_JSON.XML_to_JSON;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class testXMLtoJSON {

    @Test
    public void tryParse1lvl() throws ParserConfigurationException, IOException, SAXException {
        gamePublisher publisher = new gamePublisher("Rockstar");
        Assert.assertEquals(XML_to_JSON.parseXML("src/test/resources/TestInput.xml").getPublishers().get(0).getName(),
                publisher.getName());
    }
    @Test
    public void tryParse2lvl() throws ParserConfigurationException, IOException, SAXException {
        devStudio developerStudio = new devStudio("Rockstar Toronto", 1981, "www.rockstartoronto.com");
        Assert.assertEquals(XML_to_JSON.parseXML("src/test/resources/TestInput.xml").getPublishers().get(0).
                        getDevStudios().get(0).getName(),
                developerStudio.getName());

        Assert.assertEquals(XML_to_JSON.parseXML("src/test/resources/TestInput.xml").getPublishers().get(0).
                        getDevStudios().get(0).getYearOfFoundation(),
                developerStudio.getYearOfFoundation());

        Assert.assertEquals(XML_to_JSON.parseXML("src/test/resources/TestInput.xml").getPublishers().get(0).
                        getDevStudios().get(0).getURL(),
                developerStudio.getURL());
    }

    @Test
    public void tryParseAll() throws ParserConfigurationException, IOException, SAXException {
        XML publishers = XML_to_JSON.parseXML("src/test/resources/TestInput.xml");

        //publisher
        gamePublisher publisher = publishers.getPublishers().get(0);
        Assert.assertEquals(publisher.getName(), "Rockstar");

        //developer

        devStudio devStudio = publisher.getDevStudios().get(0);
        Assert.assertEquals(devStudio.getName(), "Rockstar Toronto");

        Assert.assertEquals(devStudio.getYearOfFoundation(), 1981);

        Assert.assertEquals(devStudio.getURL(), "www.rockstartoronto.com");

        //games
        game game = devStudio.getGames().get(0);
        Assert.assertEquals(game.getName(), "The Warriors");
        Assert.assertEquals(game.getYear(), 2005);

        game = devStudio.getGames().get(1);
        Assert.assertEquals(game.getName(), "Manhunt 2");
        Assert.assertEquals(game.getYear(), 2007);

        //platforms
        platform platform = devStudio.getGames().get(0).getPlatforms().get(0);
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
}
