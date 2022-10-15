import FileConverter.Classes.JSON.JSON;
import FileConverter.Classes.JSON.JSONgame;
import FileConverter.Classes.XML.*;
import FileConverter.JSON_to_XML.JSON_to_XML;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;


public class TestJSONtoXML {
    @Test
    public void tryParse1lvl() throws IOException {
        JSONgame game = new JSONgame("The Warriors", 2005, "Rockstar");
        JSON some = JSON_to_XML.parseJSON("src/test/resources/TestInput.json");
        assert some != null;
        JSONgame oneGame = some.getGames().get(0);
        Assert.assertEquals(oneGame.getName(), game.getName());
        Assert.assertEquals(oneGame.getYear(), game.getYear());
        Assert.assertEquals(oneGame.getGamePublisher(), game.getGamePublisher());
    }
    @Test
    public void tryParseSecondObject() throws IOException {
        JSONgame game = new JSONgame("Manhunt 2", 2007, "Rockstar");
        JSON some = JSON_to_XML.parseJSON("src/test/resources/TestInput.json");
        assert some != null;
        JSONgame oneGame = some.getGames().get(1);
        Assert.assertEquals(oneGame.getName(), game.getName());
        Assert.assertEquals(oneGame.getYear(), game.getYear());
        Assert.assertEquals(oneGame.getGamePublisher(), game.getGamePublisher());
    }
    @Test
    public void tryParseAll() throws IOException {
        JSON some = JSON_to_XML.parseJSON("src/test/resources/TestInput.json");
        JSONgame oneGame = some.getGames().get(0);

        Assert.assertEquals(oneGame.getPlatforms().get(0).getName(), "PlayStation 2");
        Assert.assertEquals(oneGame.getPlatforms().get(1).getName(), "PlayStation Portable");
        Assert.assertEquals(oneGame.getPlatforms().get(2).getName(), "XBox");

        Assert.assertEquals(oneGame.getDevStudios().get(0).getName(), "Rockstar toronto");
        Assert.assertEquals(oneGame.getDevStudios().get(0).getYearOfFoundation(), 1981);
        Assert.assertEquals(oneGame.getDevStudios().get(0).getURL(), "www.rockstartoronto.com");


        oneGame = some.getGames().get(1);

        Assert.assertEquals(oneGame.getPlatforms().get(0).getName(), "Microsoft Windows");
        Assert.assertEquals(oneGame.getPlatforms().get(1).getName(), "PlayStation 2");
        Assert.assertEquals(oneGame.getPlatforms().get(2).getName(), "PlayStation Portable");
        Assert.assertEquals(oneGame.getPlatforms().get(3).getName(), "Wii");

        Assert.assertEquals(oneGame.getDevStudios().get(0).getName(), "Rockstar toronto");
        Assert.assertEquals(oneGame.getDevStudios().get(0).getYearOfFoundation(), 1981);
        Assert.assertEquals(oneGame.getDevStudios().get(0).getURL(), "www.rockstartoronto.com");

    }

    @Test
    public void tryConvertXMLtoJSONAll() throws IOException {
        JSON_to_XML.parseJSON("src/test/resources/TestInput.json");

        XML compare = JSON_to_XML.convert();
        XMLgamePublisher gamePublisher = compare.getPublishers().get(0);
        XMLdevStudio dev = gamePublisher.getDevStudios().get(0);
        XMLgame game = dev.getGames().get(0);
        ArrayList<XMLplatform> platforms = game.getPlatforms();

        Assert.assertEquals(gamePublisher.getName(),"Rockstar");

        Assert.assertEquals(dev.getName(),"Rockstar Toronto");
        Assert.assertEquals(dev.getYearOfFoundation(),1981);
        Assert.assertEquals(dev.getURL(),"www.rockstartoronto.com");

        Assert.assertEquals(game.getName(),"The Warriors");
        Assert.assertEquals(game.getYear(),2005);

        Assert.assertEquals(platforms.get(0).getName(),"PlayStation 2");
        Assert.assertEquals(platforms.get(1).getName(),"PlayStation Portable");
        Assert.assertEquals(platforms.get(2).getName(),"XBox");

    }
}
