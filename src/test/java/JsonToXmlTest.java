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

import static org.junit.Assert.assertThrows;

public class JsonToXmlTest {

    private static final JsonToXml JSON_TO_XML_PARSER = new JsonToXml();

    @Test
    public void tryParseJson() throws IOException {
        JsonUpperClass some = JSON_TO_XML_PARSER.parseJson("src\\test\\resources\\TestInput.json");
        JsonGame oneGame = some.getGames().get(0);

        Assert.assertEquals("The Warriors", oneGame.getName());
        Assert.assertEquals(2005, oneGame.getYear());
        Assert.assertEquals("Rockstar", oneGame.getGamePublisher());
        Assert.assertEquals("PlayStation 2", oneGame.getPlatforms().get(0).getName());
        Assert.assertEquals("PlayStation Portable", oneGame.getPlatforms().get(1).getName());
        Assert.assertEquals("XBox", oneGame.getPlatforms().get(2).getName());

        Assert.assertEquals("Rockstar Toronto", oneGame.getDevStudios().get(0).getName());
        Assert.assertEquals(1981, oneGame.getDevStudios().get(0).getYearOfFoundation());
        Assert.assertEquals("www.rockstartoronto.com", oneGame.getDevStudios().get(0).getUrl());


        oneGame = some.getGames().get(1);
        Assert.assertEquals("Manhunt 2", oneGame.getName());
        Assert.assertEquals(2007, oneGame.getYear());
        Assert.assertEquals("Rockstar", oneGame.getGamePublisher());

        Assert.assertEquals("Microsoft Windows", oneGame.getPlatforms().get(0).getName());
        Assert.assertEquals("PlayStation 2", oneGame.getPlatforms().get(1).getName());
        Assert.assertEquals("PlayStation Portable", oneGame.getPlatforms().get(2).getName());
        Assert.assertEquals("Wii", oneGame.getPlatforms().get(3).getName());

        Assert.assertEquals("Rockstar Toronto", oneGame.getDevStudios().get(0).getName());
        Assert.assertEquals(1981, oneGame.getDevStudios().get(0).getYearOfFoundation());
        Assert.assertEquals("www.rockstartoronto.com", oneGame.getDevStudios().get(0).getUrl());

    }

    @Test
    public void tryConvertXmlToJson() throws IOException {
        JsonUpperClass json = JSON_TO_XML_PARSER.parseJson("src/test/resources/TestInput.json");
        XmlUpperClass compare = JSON_TO_XML_PARSER.convert(json);

        XmlGamePublisher gamePublisher = compare.getPublishers().get(0);
        Assert.assertEquals("Rockstar", gamePublisher.getName());

        XmlDevStudio dev = gamePublisher.getDevStudios().get(0);
        Assert.assertEquals("Rockstar Toronto", dev.getName());
        Assert.assertEquals(1981, dev.getYearOfFoundation());
        Assert.assertEquals("www.rockstartoronto.com", dev.getUrl());

        XmlGame game = dev.getGames().get(0);
        Assert.assertEquals("The Warriors", game.getName());
        Assert.assertEquals(2005, game.getYear());

        ArrayList<XmlPlatform> platforms = game.getPlatforms();
        Assert.assertEquals("PlayStation 2", platforms.get(0).getName());
        Assert.assertEquals("PlayStation Portable", platforms.get(1).getName());
        Assert.assertEquals("XBox", platforms.get(2).getName());
    }

    @Test
    public void tryCreateXml() throws IOException, XMLStreamException {
        JsonUpperClass json = JSON_TO_XML_PARSER.parseJson("src/test/resources/TestInput.json");
        XmlUpperClass converted = JSON_TO_XML_PARSER.convert(json);
        JSON_TO_XML_PARSER.createXML(converted, "src/test/resources/NewXML.xml");

        File fl = new File("src/test/resources/NewXML.xml");

        Assert.assertTrue(fl.exists());
    }

    @Test
    public void wrongFile() {
        Exception exception = assertThrows(Exception.class,
            () -> JSON_TO_XML_PARSER.parseJson("src/test/resources/NoSuchFile.json"));

        Assert.assertEquals("Неверный путь к файлу.", exception.getMessage());
    }
}
