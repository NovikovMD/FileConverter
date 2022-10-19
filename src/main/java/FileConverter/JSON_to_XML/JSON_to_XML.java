package FileConverter.JSON_to_XML;

import FileConverter.Classes.JSON.JSON;
import FileConverter.Classes.JSON.JSONdevStudio;
import FileConverter.Classes.JSON.JSONgame;
import FileConverter.Classes.XML.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class JSON_to_XML {

    private static JSON games = new JSON();

    public static JSON parseJSON(String path) throws IOException {
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
                if (parser.getCurrentName() == null)
                    continue;
                String key = parser.getCurrentName();

                if (key.equals("name")) {
                    games.addGame("place_holder", -1, "place_holder");
                    parser.nextToken();
                    games.returnLastGame().setName(parser.getText());
                } else if (key.equals("year")) {
                    parser.nextToken();
                    games.returnLastGame().setYear(Integer.parseInt(parser.getText()));
                } else if (key.equals("gamePublisher")) {
                    parser.nextToken();
                    games.returnLastGame().setGamePublisher(parser.getText());
                } else if (key.equals("platforms")) {
                    parser.nextToken();
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        parser.nextToken();
                        if (parser.getCurrentName().equals("name")) {
                            parser.nextToken();
                            games.returnLastGame().addPlatform(parser.getText());
                            parser.nextToken();
                        }
                    }
                } else if (key.equals("devStudios")) {
                    parser.nextToken();
                    parser.nextToken();

                    //loop until end of devStudios
                    while (parser.nextToken() != JsonToken.END_ARRAY) {

                        //checker if we're looking at "{" / "}"
                        if (parser.getCurrentName() == null)
                            continue;
                        if (parser.getCurrentName().equals("name")) {
                            parser.nextToken();
                            games.returnLastGame().addDevStudio(parser.getText(), -1, "place_holder");
                        } else if (parser.getCurrentName().equals("yearOfFoundation")) {
                            parser.nextToken();
                            games.returnLastGame().returnLastDevStudio().setYearOfFoundation(Integer.parseInt(parser.getText()));
                        } else if (parser.getCurrentName().equals("url")) {
                            parser.nextToken();
                            games.returnLastGame().returnLastDevStudio().setURL(parser.getText());
                        }
                    }
                }

            }
        } else
            return null;


        //Getting the token name


        return games;
    }

    public static XML convert() {
        XML gameIndustry = new XML();

        //getting started
        gameIndustry.addPublisher(games.getGames().get(0).getGamePublisher());


        for (int i=0;i<games.returnLength();i++){
            //get current game in json
            JSONgame jsonGame = games.getGames().get(i);

            //check if we need to create new publisher
            if (!checkIfPublExists(jsonGame.getGamePublisher(),gameIndustry.getPublishers()))
                gameIndustry.addPublisher(jsonGame.getGamePublisher());

            XMLgamePublisher XMLpublisher = findPubl(jsonGame.getGamePublisher(),gameIndustry.getPublishers());

            //collect platforms
            ArrayList<String> XMLplatforms = new ArrayList<>();
            for (int j=0;j<jsonGame.getPlatforms().size();j++){
                XMLplatforms.add(jsonGame.getPlatforms().get(j).getName());
            }

            for (int j=0;j<jsonGame.getDevStudios().size();j++){

                //get current devStudio in json
                JSONdevStudio jsonDevStudio = jsonGame.getDevStudios().get(j);

                XMLdevStudio XMLdev;
                //check if we need to create new dev
                if (!checkIfDevExists(jsonDevStudio.getName(), XMLpublisher.getDevStudios())){
                    //if it doesn't exist
                    //update gamePublisher to avoid "place_holder"-----------------------------
                    XMLpublisher.setName(jsonGame.getGamePublisher());

                    //create new dev
                    XMLpublisher.addDevStudio(jsonDevStudio.getName(),jsonDevStudio.getYearOfFoundation(),jsonDevStudio.getURL());
                }
                XMLdev = findDev(jsonDevStudio.getName(), XMLpublisher.getDevStudios());

                //add game
                XMLdev.addGame(jsonGame.getName(), jsonGame.getYear());

                //set all platforms
                XMLgame XMLgame = XMLdev.getGames().get(XMLdev.returnLength()-1);
                for (String xmlPlatform : XMLplatforms) {
                    XMLgame.addPlatform(xmlPlatform);
                }
            }
        }

        return gameIndustry;
    }

    public static void createXML(XML xmlClass, String path) {
        try(FileOutputStream out = new FileOutputStream(path)){
            writeXml(out, xmlClass);
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private static void writeXml(OutputStream out, XML xmlClass) throws XMLStreamException {
        //stax cursor
        XMLOutputFactory output = XMLOutputFactory.newInstance();

        XMLStreamWriter writer = output.createXMLStreamWriter(out);

        writer.writeStartDocument("utf-8", "1.0");

        //header
        writer.writeStartElement("GameIndustry");


        writer.writeStartElement("gamePublishers");


        // insides

        for (int i = 0; i < xmlClass.returnLength(); i++) {
            writer.writeStartElement("gamePublisher");
            writer.writeAttribute("name", xmlClass.getPublishers().get(i).getName());

            writer.writeStartElement("developerStudios");


            for (int j = 0; j < xmlClass.getPublishers().get(i).getDevStudios().size(); j++) {
                XMLdevStudio dev = xmlClass.getPublishers().get(i).getDevStudios().get(j);

                writer.writeStartElement("developerStudio");
                writer.writeAttribute("name", dev.getName());
                writer.writeAttribute("year_of_foundation", ((Integer)dev.getYearOfFoundation()).toString());
                writer.writeAttribute("URL", dev.getURL());

                writer.writeStartElement("games");

                for (int k = 0; k < dev.getGames().size(); k++) {
                    XMLgame game = dev.getGames().get(k);

                    writer.writeStartElement("game");
                    writer.writeAttribute("name", game.getName());
                    writer.writeAttribute("year", ((Integer)game.getYear()).toString());

                    writer.writeStartElement("platforms");

                    for (int l = 0; l < game.getPlatforms().size(); l++) {
                        XMLplatform xmLplatform = game.getPlatforms().get(l);

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
    private static boolean checkIfDevExists(String name, ArrayList<XMLdevStudio> devs){
        for (XMLdevStudio dev : devs) {
            if (dev.getName().equals(name))
                return true;
        }
        return false;
    }
    private static XMLdevStudio findDev(String name, ArrayList<XMLdevStudio> devs){
        for (int i=devs.size()-1;i>=0;i--){
            if (devs.get(i).getName().equals(name))
                return devs.get(i);
        }
        return null;
    }
    private static boolean checkIfPublExists(String name, ArrayList<XMLgamePublisher> publs){
        for (XMLgamePublisher publ : publs) {
            if (publ.getName().equals(name))
                return true;
        }
        return false;
    }
    private static XMLgamePublisher findPubl(String name, ArrayList<XMLgamePublisher> publs){
        for (int i=publs.size()-1;i>=0;i--){
            if (publs.get(i).getName().equals(name))
                return publs.get(i);
        }
        return null;
    }

    //endregion

}
