import FileConverter.Classes.JSON.JsonUpperClass;
import FileConverter.Classes.JSON.JsonGame;
import FileConverter.Classes.XML.*;
import FileConverter.JSON_to_XML.JsonToXml;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class JsonToXmlTest {
    @Test
    public void tryParse1lvl() throws IOException {
        JsonGame game = new JsonGame("The Warriors", 2005, "Rockstar");
        JsonUpperClass some = JsonToXml.parseJson("src/test/resources/TestInput.json");
        assert some != null;
        JsonGame oneGame = some.getGames().get(0);
        Assert.assertEquals(oneGame.getName(), game.getName());
        Assert.assertEquals(oneGame.getYear(), game.getYear());
        Assert.assertEquals(oneGame.getGamePublisher(), game.getGamePublisher());
    }

    @Test
    public void tryParseSecondObject() throws IOException {
        JsonGame game = new JsonGame("Manhunt 2", 2007, "Rockstar");
        JsonUpperClass some = JsonToXml.parseJson("src/test/resources/TestInput.json");
        assert some != null;
        JsonGame oneGame = some.getGames().get(1);
        Assert.assertEquals(oneGame.getName(), game.getName());
        Assert.assertEquals(oneGame.getYear(), game.getYear());
        Assert.assertEquals(oneGame.getGamePublisher(), game.getGamePublisher());
    }

    @Test
    public void tryParseAll() throws IOException {
        JsonUpperClass some = JsonToXml.parseJson("src\\test\\resources\\TestInput.json");
        JsonGame oneGame = some.getGames().get(0);

        Assert.assertEquals(oneGame.getPlatforms().get(0).getName(), "PlayStation 2");
        Assert.assertEquals(oneGame.getPlatforms().get(1).getName(), "PlayStation Portable");
        Assert.assertEquals(oneGame.getPlatforms().get(2).getName(), "XBox");

        Assert.assertEquals(oneGame.getDevStudios().get(0).getName(), "Rockstar Toronto");
        Assert.assertEquals(oneGame.getDevStudios().get(0).getYearOfFoundation(), 1981);
        Assert.assertEquals(oneGame.getDevStudios().get(0).getUrl(), "www.rockstartoronto.com");


        oneGame = some.getGames().get(1);

        Assert.assertEquals(oneGame.getPlatforms().get(0).getName(), "Microsoft Windows");
        Assert.assertEquals(oneGame.getPlatforms().get(1).getName(), "PlayStation 2");
        Assert.assertEquals(oneGame.getPlatforms().get(2).getName(), "PlayStation Portable");
        Assert.assertEquals(oneGame.getPlatforms().get(3).getName(), "Wii");

        Assert.assertEquals(oneGame.getDevStudios().get(0).getName(), "Rockstar Toronto");
        Assert.assertEquals(oneGame.getDevStudios().get(0).getYearOfFoundation(), 1981);
        Assert.assertEquals(oneGame.getDevStudios().get(0).getUrl(), "www.rockstartoronto.com");

    }

    @Test
    public void tryConvertXMLtoJSONAll() throws IOException {
        JsonToXml.parseJson("src/test/resources/TestInput.json");

        XmlUpperClass compare = JsonToXml.convert();
        XmlGamePublisher gamePublisher = compare.getPublishers().get(0);
        XmlDevStudio dev = gamePublisher.getDevStudios().get(0);
        XmlGame game = dev.getGames().get(0);
        ArrayList<XmlPlatform> platforms = game.getPlatforms();

        Assert.assertEquals(gamePublisher.getName(), "Rockstar");

        Assert.assertEquals(dev.getName(), "Rockstar Toronto");
        Assert.assertEquals(dev.getYearOfFoundation(), 1981);
        Assert.assertEquals(dev.getUrl(), "www.rockstartoronto.com");

        Assert.assertEquals(game.getName(), "The Warriors");
        Assert.assertEquals(game.getYear(), 2005);

        Assert.assertEquals(platforms.get(0).getName(), "PlayStation 2");
        Assert.assertEquals(platforms.get(1).getName(), "PlayStation Portable");
        Assert.assertEquals(platforms.get(2).getName(), "XBox");

    }

    @Test
    public void tryCreteXML() throws IOException {
        JsonToXml.parseJson("src/test/resources/TestInput.json");
        XmlUpperClass converted = JsonToXml.convert();
        JsonToXml.createXML(converted, "src/test/resources/NewXML.xml");

        File fl = new File("src/test/resources/NewXML.xml");

        Assert.assertTrue(fl.exists());
    }
}
