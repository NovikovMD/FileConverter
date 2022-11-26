/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package file_converter.xml_to_json;

import file_converter.classes.json.*;
import file_converter.classes.xml.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Provides three methods:
 * 1) parseXml - reads value from XML file using SAX.
 * 2) convert - converts XML classes to Json classes.
 * 3) createJson - creates Json file using Jackson-databind.
 * <p>
 * Extends DefaultHandler to XMLHandler to provide parsing of XML file using SAX
 *
 * @author Novikov Matthew
 */
public class XmlToJson {
    private XmlUpperClass gameIndustry;
    private final SAXParserFactory factory = SAXParserFactory.newInstance();
    private final XmlHandler handler = new XmlHandler();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Reads value from XML file.
     *
     * @param path absolute path to existing XML file.
     * @return XML data holder if needed.
     */
    public XmlUpperClass parseXml(final String path) throws ParserConfigurationException, SAXException, IOException {
        SAXParser parser = factory.newSAXParser();

        File fl = new File(path);
        parser.parse(fl, handler);

        return gameIndustry;
    }

    /**
     * Converts XML classes to Json classes.
     *
     * @return Json data holder class.
     */
    public JsonUpperClass convert(XmlUpperClass gameIndustry) {
        JsonUpperClass jsonUpperClassGames = new JsonUpperClass();

        startConvert(gameIndustry, jsonUpperClassGames);

        return jsonUpperClassGames;
    }

    //region convert private methods
    private void startConvert(XmlUpperClass gameIndustry, JsonUpperClass jsonUpperClassGames) {
        for (int i = 0; i < gameIndustry.returnLength(); i++) {
            //get current publisher
            XmlGamePublisher publisher = gameIndustry.getPublishers().get(i);

            getPublisher(jsonUpperClassGames, publisher);
        }
    }

    private void getPublisher(JsonUpperClass jsonUpperClassGames, XmlGamePublisher publisher) {
        for (int j = 0; j < publisher.returnLength(); j++) {
            //get current developer
            XmlDevStudio developer = publisher.getDevStudios().get(j);

            getDeveloper(jsonUpperClassGames, publisher, developer);
        }
    }

    private void getDeveloper(JsonUpperClass jsonUpperClassGames, XmlGamePublisher publisher, XmlDevStudio developer) {
        for (int k = 0; k < developer.returnLength(); k++) {
            //get current game
            XmlGame game = developer.getGames().get(k);

            getGame(jsonUpperClassGames, publisher, developer, game);
        }
    }

    private void getGame(JsonUpperClass jsonUpperClassGames, XmlGamePublisher publisher, XmlDevStudio developer, XmlGame game) {
        JsonGame checker = getCurrentGame(game.getName(), jsonUpperClassGames.getGames());
        //add current game to list, if it doesn't exist so far
        if (checker == null) {
            createNewGame(jsonUpperClassGames, publisher, developer, game);
        } else {
            //if we have current game in list, we need to add developer studio to it
            checker.addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());
        }
    }
    private JsonGame getCurrentGame(String nameToFind, ArrayList<JsonGame> listToLookIn) {
        JsonGame foundGame = null;

        for (JsonGame jsoNgame : listToLookIn) {
            if (jsoNgame.getName().equals(nameToFind)) {
                foundGame = jsoNgame;
            }
        }

        return foundGame;
    }

    private void createNewGame(JsonUpperClass jsonUpperClassGames, XmlGamePublisher publisher,
                                      XmlDevStudio developer, XmlGame game) {
        jsonUpperClassGames.addGame(game.getName(), game.getYear(), publisher.getName());
        JsonGame jsonGame = jsonUpperClassGames.getGames().get(jsonUpperClassGames.returnLength() - 1);

        getPlatform(game, jsonGame);

        jsonGame.addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());
    }
    private  void getPlatform(XmlGame game, JsonGame jsonGame) {
        for (int l = 0; l < game.returnLength(); l++) {
            XmlPlatform platform = game.getPlatforms().get(l);

            jsonGame.addPlatform(platform.getName());
        }
    }
    //endregion

    /**
     * Creates Json file.
     *
     * @param jsonUpperClass Json data holder class (Filled in convert method).
     * @param path           absolute path to new Json file.
     */
    public void createJson(JsonUpperClass jsonUpperClass, String path) throws IOException {
        mapper.writeValue(new File(path), jsonUpperClass);
    }

    private class XmlHandler extends DefaultHandler {
        public void startDocument() {
            gameIndustry = new XmlUpperClass();
        }
        /**
         * Provides parsing of XML file using DefaultHandler.
         *
         * @param uri        The Namespace URI, or the empty string if the
         *                   element has no Namespace URI or if Namespace
         *                   processing is not being performed.
         * @param localName  The local name (without prefix), or the
         *                   empty string if Namespace processing is not being
         *                   performed.
         * @param qName      The qualified name (with prefix), or the
         *                   empty string if qualified names are not available.
         * @param attributes The attributes attached to the element.  If
         *                   there are no attributes, it shall be an empty
         *                   Attributes object.
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            switch (qName) {
                case "gamePublisher" -> {
                    getGamePublisherSAX(attributes);
                }
                case "developerStudio" -> {
                    getDeveloperStudioSAX(attributes);
                }
                case "game" -> {
                    getGameSAX(attributes);
                }
                case "platform" -> {
                    getPlatformSAX(attributes);
                }
            }
        }

        private void getGamePublisherSAX(Attributes attributes) {
            String name = attributes.getValue("name");
            gameIndustry.addPublisher(name);
        }

        private void getDeveloperStudioSAX(Attributes attributes) {
            String name = attributes.getValue("name");
            int year = Integer.parseInt(attributes.getValue("year_of_foundation"));
            String URL = attributes.getValue("URL");
            gameIndustry.getPublishers().get(gameIndustry.returnLength() - 1).addDevStudio(name, year, URL);
        }

        private void getGameSAX(Attributes attributes) {
            String name = attributes.getValue("name");
            int year = Integer.parseInt(attributes.getValue("year"));

            int gameIndustryListLength = gameIndustry.returnLength() - 1;

            XmlGamePublisher publisher = gameIndustry.getPublishers().get(gameIndustryListLength);
            int publisherListLength = publisher.returnLength() - 1;

            XmlDevStudio devStudio = publisher.getDevStudios().get(publisherListLength);

            devStudio.addGame(name, year);
        }

        private void getPlatformSAX(Attributes attributes) {
            String name = attributes.getValue("name");

            int gameIndustryListLength = gameIndustry.returnLength() - 1;

            XmlGamePublisher publisher = gameIndustry.getPublishers().get(gameIndustryListLength);
            int publisherListLength = publisher.returnLength() - 1;

            XmlDevStudio devStudio = publisher.getDevStudios().get(publisherListLength);
            int devStudioListLength = devStudio.returnLength() - 1;

            XmlGame game = devStudio.getGames().get(devStudioListLength);
            game.addPlatform(name);
        }
    }

}
