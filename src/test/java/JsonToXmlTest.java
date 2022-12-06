import fileconverter.bean.json.JsonGame;
import fileconverter.bean.xml.*;
import fileconverter.jsontoxml.JsonToXml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JsonToXmlTest {

    private static final JsonToXml JSON_TO_XML_PARSER = new JsonToXml();

    @Test
    public void tryParseJson() throws IOException {
        JsonGame oneGame = JSON_TO_XML_PARSER.parseJson("src\\test\\resources\\TestInput.json")
            .getGames().get(0);

        assertEquals("The Warriors", oneGame.getName());
        assertEquals(2005, oneGame.getYear());
        assertEquals("Rockstar", oneGame.getGamePublisher());
        assertEquals("PlayStation 2", oneGame.getPlatforms().get(0).getName());
        assertEquals("PlayStation Portable", oneGame.getPlatforms().get(1).getName());
        assertEquals("XBox", oneGame.getPlatforms().get(2).getName());

        assertEquals("Rockstar Toronto", oneGame.getDevStudios().get(0).getName());
        assertEquals(1981, oneGame.getDevStudios().get(0).getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", oneGame.getDevStudios().get(0).getUrl());


        oneGame = JSON_TO_XML_PARSER.parseJson("src\\test\\resources\\TestInput.json").getGames().get(1);
        assertEquals("Manhunt 2", oneGame.getName());
        assertEquals(2007, oneGame.getYear());
        assertEquals("Rockstar", oneGame.getGamePublisher());

        assertEquals("Microsoft Windows", oneGame.getPlatforms().get(0).getName());
        assertEquals("PlayStation 2", oneGame.getPlatforms().get(1).getName());
        assertEquals("PlayStation Portable", oneGame.getPlatforms().get(2).getName());
        assertEquals("Wii", oneGame.getPlatforms().get(3).getName());

        assertEquals("Rockstar Toronto", oneGame.getDevStudios().get(0).getName());
        assertEquals(1981, oneGame.getDevStudios().get(0).getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", oneGame.getDevStudios().get(0).getUrl());

    }

    @Test
    public void tryConvertXmlToJson() throws IOException {
        XmlGamePublisher gamePublisher = JSON_TO_XML_PARSER.convert(
                JSON_TO_XML_PARSER.parseJson("src/test/resources/TestInput.json"))
            .getPublishers().get(0);
        assertEquals("Rockstar", gamePublisher.getName());

        XmlDevStudio dev = gamePublisher.getDevStudios().get(0);
        assertEquals("Rockstar Toronto", dev.getName());
        assertEquals(1981, dev.getYearOfFoundation());
        assertEquals("www.rockstartoronto.com", dev.getUrl());

        XmlGame game = dev.getGames().get(0);
        assertEquals("The Warriors", game.getName());
        assertEquals(2005, game.getYear());

        ArrayList<XmlPlatform> platforms = game.getPlatforms();
        assertEquals("PlayStation 2", platforms.get(0).getName());
        assertEquals("PlayStation Portable", platforms.get(1).getName());
        assertEquals("XBox", platforms.get(2).getName());
    }

    @Test
    public void tryCreateXml() throws IOException, XMLStreamException {
        JSON_TO_XML_PARSER.createXML(
            JSON_TO_XML_PARSER.convert(
                JSON_TO_XML_PARSER.parseJson("src/test/resources/TestInput.json")),
            "src/test/resources/NewXML.xml");

        assertTrue(new File("src/test/resources/NewXML.xml").exists());
    }

    @Test
    public void wrongFile() {
        assertEquals("Неверный путь к файлу.",
            assertThrows(Exception.class,
                () -> JSON_TO_XML_PARSER.parseJson("src/test/resources/NoSuchFile.json"))
                .getMessage());
    }
}
