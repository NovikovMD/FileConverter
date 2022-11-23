/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package FileConverter.XML_to_JSON;

import FileConverter.Classes.JSON.*;
import FileConverter.Classes.XML.*;
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
public class XmlToJson extends DefaultHandler {
    /* Stores data from XML file. Filled in during parseXml method. */
    private static final XmlUpperClass GAME_INDUSTRY = new XmlUpperClass();

    public static XmlUpperClass getGameIndustry() {
        return GAME_INDUSTRY;
    }

    /**
     * Reads value from XML file.
     *
     * @param path absolute path to existing XML file.
     * @return XML data holder if needed.
     */
    public static XmlUpperClass parseXml(String path) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = null;

        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException("Failed to create SAXParser\n" + e);
        }

        XmlHandler handler = new XmlHandler();
        File fl = new File(path);

        try {
            parser.parse(fl, handler);
        } catch (SAXException | IOException e) {
            throw new RuntimeException("Parser caused exception:\n" + e);
        }

        return GAME_INDUSTRY;
    }

    /**
     * Converts XML classes to Json classes.
     *
     * @return Json data holder class.
     */
    public static JsonUpperClass convert() {
        JsonUpperClass jsonUpperClassGames = new JsonUpperClass();

        for (int i = 0; i < GAME_INDUSTRY.returnLength(); i++) {
            //get current publisher
            XmlGamePublisher publisher = GAME_INDUSTRY.getPublishers().get(i);

            for (int j = 0; j < publisher.returnLength(); j++) {
                //get current developer
                XmlDevStudio developer = publisher.getDevStudios().get(j);

                for (int k = 0; k < developer.returnLength(); k++) {
                    //get current game
                    XmlGame game = developer.getGames().get(k);

                    JsonGame checker = getCurrentGame(game.getName(), jsonUpperClassGames.getGames());
                    //add current game to list, if it doesn't exist so far
                    if (checker == null) {
                        //add new game to JSON list
                        jsonUpperClassGames.addGame(game.getName(), game.getYear(), publisher.getName());

                        //create variable to collect platforms
                        JsonGame jsonGame = jsonUpperClassGames.getGames().get(jsonUpperClassGames.returnLength() - 1);

                        //collect all platforms
                        for (int l = 0; l < game.returnLength(); l++) {
                            XmlPlatform platform = game.getPlatforms().get(l);

                            //add platform to JSON.game list
                            jsonGame.addPlatform(platform.getName());
                        }

                        //add developer studio to current game
                        jsonGame.addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());
                    } else {
                        //if we have current game in list, we need to add developer studio to it
                        checker.addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());
                    }
                }
            }
        }

        return jsonUpperClassGames;
    }

    /**
     * Creates Json file.
     *
     * @param jsonUpperClass Json data holder class (Filled in convert method).
     * @param path           absolute path to new Json file.
     */
    public static void createJson(JsonUpperClass jsonUpperClass, String path) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(new File(path), jsonUpperClass);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write value to json file\n" + e);
        }
    }

    private static JsonGame getCurrentGame(String nameToFind, ArrayList<JsonGame> listToLookIn) {
        JsonGame foundGame = null;

        for (JsonGame jsoNgame : listToLookIn) {
            if (jsoNgame.getName().equals(nameToFind)) {
                foundGame = jsoNgame;
            }
        }

        return foundGame;
    }


    private static class XmlHandler extends DefaultHandler {
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
            if (qName.equals("gamePublisher")) {
                String name = attributes.getValue("name");
                GAME_INDUSTRY.addPublisher(name);
            } else if (qName.equals("developerStudio")) {
                String name = attributes.getValue("name");
                Integer year = Integer.parseInt(attributes.getValue("year_of_foundation"));
                String URL = attributes.getValue("URL");
                GAME_INDUSTRY.getPublishers().get(GAME_INDUSTRY.returnLength() - 1).addDevStudio(name, year, URL);
            } else if (qName.equals("game")) {
                String name = attributes.getValue("name");
                int year = Integer.parseInt(attributes.getValue("year"));
                GAME_INDUSTRY.getPublishers().get(GAME_INDUSTRY.returnLength() - 1).getDevStudios()
                        .get(GAME_INDUSTRY.getPublishers().get(GAME_INDUSTRY.returnLength() - 1).returnLength() - 1)
                        .addGame(name, year);

            } else if (qName.equals("platform")) {
                String name = attributes.getValue("name");

                int gameIndustryListLength = GAME_INDUSTRY.returnLength() - 1;

                XmlGamePublisher publisher = GAME_INDUSTRY.getPublishers().get(gameIndustryListLength);
                int publisherListLength = publisher.returnLength() - 1;

                XmlDevStudio devStudio = publisher.getDevStudios().get(publisherListLength);
                int devStudioListLength = devStudio.returnLength() - 1;

                XmlGame game = devStudio.getGames().get(devStudioListLength);
                game.addPlatform(name);
            }
        }
    }

}
