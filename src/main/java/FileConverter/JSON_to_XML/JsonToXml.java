/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package FileConverter.JSON_to_XML;

import FileConverter.Classes.JSON.JsonUpperClass;
import FileConverter.Classes.JSON.JsonDevStudio;
import FileConverter.Classes.JSON.JsonGame;
import FileConverter.Classes.XML.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.ArrayList;

/**
 * Provides three methods:
 * 1) parseJson - reads value from Json file using Jackson.
 * 2) convert - converts Json classes to XML classes.
 * 3) createXML - creates XML file using StAX.
 *
 * @author Novikov Matthew
 */
public class JsonToXml {

    /* Stores data from Json file. Filled in during parseJson method. */
    private static final JsonUpperClass GAMES = new JsonUpperClass();

    /**
     * Reads value from Json file.
     * @param path absolute path to existing Json file.
     * @return Json data holder if needed.
     * @throws IOException if failed to read Json file
     */
    public static JsonUpperClass parseJson(String path) throws IOException {
        File fl = new File(path);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(fl);

        //point to
        parser.nextToken();
        parser.nextToken();

        if (parser.nextToken() == JsonToken.START_ARRAY) {
            //loop until token equal to "]"
            while (parser.nextToken() != JsonToken.END_ARRAY) {

                //checker if we're looking at "{" / "}"
                if (parser.getCurrentName() == null) {
                    continue;
                }
                String key = parser.getCurrentName();

                if (key.equals("name")) {
                    GAMES.addGame("place_holder", -1, "place_holder");
                    parser.nextToken();
                    GAMES.returnLastGame().setName(parser.getText());
                } else if (key.equals("year")) {
                    parser.nextToken();
                    GAMES.returnLastGame().setYear(Integer.parseInt(parser.getText()));
                } else if (key.equals("gamePublisher")) {
                    parser.nextToken();
                    GAMES.returnLastGame().setGamePublisher(parser.getText());
                } else if (key.equals("platforms")) {
                    parsePlatforms(parser);
                } else if (key.equals("devStudios")) {
                    parseDevStudios(parser);
                }
            }
        } else {
            return null;
        }

        return GAMES;
    }

    /**
     * Peace of parseJson method
     * @param parser configured parser
     * @throws IOException if failed to read Json file
     */
    private static void parsePlatforms(JsonParser parser) throws IOException {
        parser.nextToken();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            parser.nextToken();
            if (parser.getCurrentName().equals("name")) {
                parser.nextToken();
                GAMES.returnLastGame().addPlatform(parser.getText());
                parser.nextToken();
            }
        }
    }

    /**
     * Peace of parseJson method
     * @param parser configured parser
     * @throws IOException if failed to read Json file
     */
    private static void parseDevStudios(JsonParser parser) throws IOException {
        parser.nextToken();
        parser.nextToken();

        //loop until end of devStudios
        while (parser.nextToken() != JsonToken.END_ARRAY) {

            //checker if we're looking at "{" / "}"
            if (parser.getCurrentName() == null) {
                continue;
            }

            if (parser.getCurrentName().equals("name")) {
                parser.nextToken();
                GAMES.returnLastGame().addDevStudio(parser.getText(), -1, "place_holder");
            } else if (parser.getCurrentName().equals("yearOfFoundation")) {
                parser.nextToken();
                GAMES.returnLastGame().returnLastDevStudio()
                        .setYearOfFoundation(Integer.parseInt(parser.getText()));
            } else if (parser.getCurrentName().equals("url")) {
                parser.nextToken();
                GAMES.returnLastGame().returnLastDevStudio().setUrl(parser.getText());
            }
        }
    }

    /**
     * converts Json classes to XML classes.
     * @return XML data holder.
     */
    public static XmlUpperClass convert() {
        XmlUpperClass gameIndustry = new XmlUpperClass();

        //getting started
        gameIndustry.addPublisher(GAMES.getGames().get(0).getGamePublisher());

        for (int i = 0; i < GAMES.returnLength(); i++) {
            //get current game in json
            JsonGame jsonGame = GAMES.getGames().get(i);

            //check if we need to create new publisher
            if (!checkIfPublisherExists(jsonGame.getGamePublisher(), gameIndustry.getPublishers())) {
                gameIndustry.addPublisher(jsonGame.getGamePublisher());
            }
            XmlGamePublisher XmlPublisher = findPublisher(jsonGame.getGamePublisher(), gameIndustry.getPublishers());

            //collect platforms
            ArrayList<String> XmlPlatforms = new ArrayList<>();
            for (int j = 0; j < jsonGame.getPlatforms().size(); j++) {
                XmlPlatforms.add(jsonGame.getPlatforms().get(j).getName());
            }

            for (int j = 0; j < jsonGame.getDevStudios().size(); j++) {
                //get current devStudio in json
                JsonDevStudio jsonDevStudio = jsonGame.getDevStudios().get(j);

                XmlDevStudio XmlDev;
                //check if we need to create new dev
                if (!checkIfDevExists(jsonDevStudio.getName(), XmlPublisher.getDevStudios())) {
                    //if it doesn't exist
                    //update gamePublisher to avoid "place_holder"
                    XmlPublisher.setName(jsonGame.getGamePublisher());

                    //create new dev
                    XmlPublisher.addDevStudio(jsonDevStudio.getName(),
                            jsonDevStudio.getYearOfFoundation(), jsonDevStudio.getUrl());
                }
                XmlDev = findDev(jsonDevStudio.getName(), XmlPublisher.getDevStudios());

                //add game
                XmlDev.addGame(jsonGame.getName(), jsonGame.getYear());

                //set all platforms
                XmlGame XMLgame = XmlDev.getGames().get(XmlDev.returnLength() - 1);
                for (String xmlPlatform : XmlPlatforms) {
                    XMLgame.addPlatform(xmlPlatform);
                }
            }
        }

        return gameIndustry;
    }

    /**
     * Starts StAX parser
     * @param xmlUpperClassClass XML data holder (Filled in convert method).
     * @param path absolute path to new XML file.
     */
    public static void createXML(XmlUpperClass xmlUpperClassClass, String path) {
        try (FileOutputStream out = new FileOutputStream(path)) {
            writeXml(out, xmlUpperClassClass);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create xml file\n" + e);
        } catch (XMLStreamException e) {
            throw new RuntimeException("Failed to write value to xml file\n" + e);
        }
    }

    /**
     * Creates XML file using StAX
     * @param out OutputStream to write the file to
     * @param xmlUpperClassClass XML data holder (Filled in convert method).
     * @throws XMLStreamException if failed to write XML file.
     */
    private static void writeXml(OutputStream out, XmlUpperClass xmlUpperClassClass) throws XMLStreamException {
        //stax cursor
        XMLOutputFactory output = XMLOutputFactory.newInstance();

        XMLStreamWriter writer = output.createXMLStreamWriter(out);

        writer.writeStartDocument("utf-8", "1.0");

        // header
        writer.writeStartElement("GameIndustry");
        writer.writeStartElement("gamePublishers");

        // creating file
        for (int i = 0; i < xmlUpperClassClass.returnLength(); i++) {
            writer.writeStartElement("gamePublisher");
            writer.writeAttribute("name", xmlUpperClassClass.getPublishers().get(i).getName());

            writer.writeStartElement("developerStudios");

            for (int j = 0; j < xmlUpperClassClass.getPublishers().get(i).getDevStudios().size(); j++) {
                XmlDevStudio dev = xmlUpperClassClass.getPublishers().get(i).getDevStudios().get(j);

                writer.writeStartElement("developerStudio");
                writer.writeAttribute("name", dev.getName());
                writer.writeAttribute("year_of_foundation", ((Integer) dev.getYearOfFoundation()).toString());
                writer.writeAttribute("URL", dev.getUrl());

                writer.writeStartElement("games");

                for (int k = 0; k < dev.getGames().size(); k++) {
                    XmlGame game = dev.getGames().get(k);

                    writer.writeStartElement("game");
                    writer.writeAttribute("name", game.getName());
                    writer.writeAttribute("year", ((Integer) game.getYear()).toString());

                    writer.writeStartElement("platforms");

                    for (int l = 0; l < game.getPlatforms().size(); l++) {
                        XmlPlatform xmLplatform = game.getPlatforms().get(l);

                        writer.writeStartElement("platform");
                        writer.writeAttribute("name", xmLplatform.getName());
                        writer.writeEndElement();
                    }
                    writer.writeEndElement();//end platforms
                    writer.writeEndElement();//end game
                }
                writer.writeEndElement();//end games
                writer.writeEndElement();//end dev
            }
            writer.writeEndElement();//end devs
            writer.writeEndElement();//end publisher
        }

        writer.writeEndElement();//end publishers
        writer.writeEndElement();//end GameIndustry

        writer.flush();
        writer.close();
    }

    //region Utils
    private static boolean checkIfDevExists(String name, ArrayList<XmlDevStudio> devs) {
        for (XmlDevStudio dev : devs) {
            if (dev.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private static XmlDevStudio findDev(String name, ArrayList<XmlDevStudio> devs) {
        for (int i = devs.size() - 1; i >= 0; i--) {
            if (devs.get(i).getName().equals(name)) {
                return devs.get(i);
            }
        }
        return null;
    }

    private static boolean checkIfPublisherExists(String name, ArrayList<XmlGamePublisher> gamePublishers) {
        for (XmlGamePublisher gamePublisher : gamePublishers) {
            if (gamePublisher.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private static XmlGamePublisher findPublisher(String name, ArrayList<XmlGamePublisher> gamePublishers) {
        for (int i = gamePublishers.size() - 1; i >= 0; i--) {
            if (gamePublishers.get(i).getName().equals(name)) {
                return gamePublishers.get(i);
            }
        }
        return null;
    }

    //endregion
}
