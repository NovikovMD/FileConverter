import file_converter.classes.json.JsonUpperClass;
import file_converter.classes.json.JsonGame;
import file_converter.classes.xml.*;
import file_converter.json_to_xml.JsonToXml;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class JsonToXmlTest {

    private static final JsonToXml jsonToXmlParser = new JsonToXml();
    @Test
    public void tryParse1lvl() throws IOException {
        JsonGame game = new JsonGame("The Warriors", 2005, "Rockstar");
        JsonUpperClass some = jsonToXmlParser.parseJson("src/test/resources/TestInput.json");
        assert some != null;
        JsonGame oneGame = some.getGames().get(0);
        Assert.assertEquals(oneGame.getName(), game.getName());
        Assert.assertEquals(oneGame.getYear(), game.getYear());
        Assert.assertEquals(oneGame.getGamePublisher(), game.getGamePublisher());
    }

    @Test
    public void tryParseSecondObject() throws IOException {
        JsonGame game = new JsonGame("Manhunt 2", 2007, "Rockstar");
        JsonUpperClass some = jsonToXmlParser.parseJson("src/test/resources/TestInput.json");
        assert some != null;
        JsonGame oneGame = some.getGames().get(1);
        Assert.assertEquals(oneGame.getName(), game.getName());
        Assert.assertEquals(oneGame.getYear(), game.getYear());
        Assert.assertEquals(oneGame.getGamePublisher(), game.getGamePublisher());
    }

    @Test
    public void tryParseAll() throws IOException {
        JsonUpperClass some = jsonToXmlParser.parseJson("src\\test\\resources\\TestInput.json");
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
        JsonUpperClass json = jsonToXmlParser.parseJson("src/test/resources/TestInput.json");

        XmlUpperClass compare = jsonToXmlParser.convert(json);
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
    public void tryCreteXML() throws IOException, XMLStreamException {
        JsonUpperClass json = jsonToXmlParser.parseJson("src/test/resources/newName.json");
        XmlUpperClass converted = jsonToXmlParser.convert(json);
        jsonToXmlParser.createXML(converted, "src/test/resources/NewXML.xml");

        File fl = new File("src/test/resources/NewXML.xml");

        Assert.assertTrue(fl.exists());
    }
}
