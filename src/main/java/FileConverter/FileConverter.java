package FileConverter;

import FileConverter.Classes.XML.XMLgamePublisher;
import FileConverter.JSON_to_XML.JSON_to_XML;
import FileConverter.XML_to_JSON.XML_to_JSON;

import java.util.ArrayList;
import java.util.List;

public class FileConverter {
    public void convertToJson(String pathToXML, String pathToNewFile)  {
        try {
            XML_to_JSON.parseXML(pathToXML);
            XML_to_JSON.createJSON(XML_to_JSON.convert(), pathToNewFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void convertToXML(String pathToJSON, String pathToNewFile)  {
        try {
            JSON_to_XML.parseJSON(pathToJSON);
            JSON_to_XML.createXML(JSON_to_XML.convert(), pathToNewFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void doSum(){
        var w = XML_to_JSON.getGameIndustry();
        for (XMLgamePublisher publisher : w.getPublishers()) {
            /*List<String> hoody = new ArrayList<>();
            hoody.add("Rockstar Toronto");
            hoody.add("Rockstar London");
            publisher.addGameToDevs(hoody,"GTA 5", 2012);*/


            //publisher.sortByGameName();

            //var get = publisher.getAllStartingWith('r');

            //var get = publisher.getAllSorted();

            //publisher.appendInAllNames("Noooo");

            int y = 2;
        }
    }
}
