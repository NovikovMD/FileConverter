package FileConverter.JSON_to_XML;

import FileConverter.Classes.JSON.JSON;
import FileConverter.Classes.JSON.JSONdevStudio;
import FileConverter.Classes.JSON.JSONgame;
import FileConverter.Classes.XML.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.File;
import java.io.IOException;
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
                    games.getLastGame().setName(parser.getText());
                } else if (key.equals("year")) {
                    parser.nextToken();
                    games.getLastGame().setYear(Integer.parseInt(parser.getText()));
                } else if (key.equals("gamePublisher")) {
                    parser.nextToken();
                    games.getLastGame().setGamePublisher(parser.getText());
                } else if (key.equals("platforms")) {
                    parser.nextToken();
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        parser.nextToken();
                        String some = parser.getText();
                        if (parser.getCurrentName().equals("name")) {
                            parser.nextToken();
                            some = parser.getText();
                            games.getLastGame().addPlatform(parser.getText());
                            parser.nextToken();
                        }
                    }
                } else if (key.equals("developer_studios")) {
                    parser.nextToken();
                    parser.nextToken();

                    //loop until end of devStudios
                    while (parser.nextToken() != JsonToken.END_ARRAY) {

                        //checker if we're looking at "{" / "}"
                        if (parser.getCurrentName() == null)
                            continue;
                        if (parser.getCurrentName().equals("name")) {
                            parser.nextToken();
                            games.getLastGame().addDevStudio(parser.getText(), -1, "place_holder");
                        } else if (parser.getCurrentName().equals("yearOfFoundation")) {
                            parser.nextToken();
                            games.getLastGame().getLastDevStudio().setYearOfFoundation(Integer.parseInt(parser.getText()));
                        } else if (parser.getCurrentName().equals("url")) {
                            parser.nextToken();
                            games.getLastGame().getLastDevStudio().setURL(parser.getText());
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
                XMLgame XMLgame = XMLdev.getGames().get(XMLdev.getLength()-1);
                for (String xmlPlatform : XMLplatforms) {
                    XMLgame.addPlatform(xmlPlatform);
                }
            }
        }

        return gameIndustry;
    }

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

}
