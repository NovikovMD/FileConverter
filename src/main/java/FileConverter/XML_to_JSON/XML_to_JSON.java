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

public class XML_to_JSON extends DefaultHandler {
    private static XML gameIndustry = new XML();

    public static XML parseXML(String path) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        XMLHandler handler = new XMLHandler();
        parser.parse(new File(path), handler);
        return gameIndustry;
    }

    public static JSON convert(){
        JSON jsonGames = new JSON();

        for (int i=0;i< gameIndustry.getLength();i++) {
            //get current publisher
            gamePublisher publisher = gameIndustry.getPublishers().get(i);

            for (int j=0;j<publisher.getLength();j++){
                //get current developer
                FileConverter.Classes.XML.devStudio developer = publisher.getDevStudios().get(j);

                for (int k=0;k<developer.getLength();k++) {
                    //get current game
                    FileConverter.Classes.XML.game game = developer.getGames().get(k);


                    FileConverter.Classes.JSON.game checker = getCurrentGame(game.getName(),jsonGames.getGames() );
                    //add current game to list, if it doesn't exist so far
                    if (checker==null) {
                        //add new game to JSON list
                        jsonGames.addGame(game.getName(), game.getYear(), publisher.getName());

                        //create variable to collect platforms
                        FileConverter.Classes.JSON.game jsonGame = jsonGames.getGames().get(jsonGames.returnLength() - 1);

                        //collect all platforms
                        for (int l = 0; l < game.getLength(); l++) {
                            FileConverter.Classes.XML.platform platform = game.getPlatforms().get(l);

                            //add platform to JSON.game list
                            jsonGame.addPlatform(platform.getName());
                        }

                        //add developer studio to current game
                        jsonGame.addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getURL());
                    }
                    else{
                        //if we have current game in list, we need to add developer studio to it

                        //adding dev studio
                        checker.addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getURL());
                    }

                }
            }
        }

        return jsonGames;
    }

    public static void createJSON(JSON json, String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(path),json);
    }

    private static FileConverter.Classes.JSON.game getCurrentGame(String nameToFind, ArrayList<FileConverter.Classes.JSON.game> listToLookIn){
        FileConverter.Classes.JSON.game foundGame = null;

        for (int i=0;i<listToLookIn.size();i++){
            if (listToLookIn.get(i).getName().equals(nameToFind))
                foundGame = listToLookIn.get(i);
        }

        return foundGame;
    }


    private static class XMLHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("gamePublisher")) {
                String name = attributes.getValue("name");
                gameIndustry.addPublisher(name);
            }
            else if (qName.equals("developerStudio")){
                String name = attributes.getValue("name");
                Integer year = Integer.parseInt(attributes.getValue("year_of_foundation"));
                String URL = attributes.getValue("URL");
                gameIndustry.getPublishers().get(gameIndustry.getLength()-1).addDevStudio(name,year,URL);
            }
            else if (qName.equals("game")){
                String name = attributes.getValue("name");
                int year = Integer.parseInt(attributes.getValue("year"));
                gameIndustry.getPublishers().get(gameIndustry.getLength()-1).getDevStudios()
                        .get(gameIndustry.getPublishers().get(gameIndustry.getLength()-1).getLength()-1).addGame(name,year);
            }
            else if (qName.equals("platform")){
                String name = attributes.getValue("name");
                gameIndustry.getPublishers().get(gameIndustry.getLength()-1).getDevStudios()
                        .get(gameIndustry.getPublishers().get(gameIndustry.getLength()-1).getLength()-1).getGames()
                        .get(gameIndustry.getPublishers().get(gameIndustry.getLength()-1).getDevStudios().
                                get(gameIndustry.getPublishers().get(gameIndustry.getLength()-1).getLength()-1).getLength()-1).addPlatform(name);
            }
        }
    }

}
