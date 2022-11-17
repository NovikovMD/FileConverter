package FileConverter.Classes.XML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class XMLgamePublisher {
    private String name;
    private ArrayList<XMLdevStudio> devStudios;

    public XMLgamePublisher(String name) {
        this.name = name;
        this.devStudios = new ArrayList<XMLdevStudio>();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addDevStudio(String name, int yearOfFoundation, String URL){
        devStudios.add(new XMLdevStudio(name,yearOfFoundation, URL));
    }

    public ArrayList<XMLdevStudio> getDevStudios(){
        return devStudios;
    }

    public int returnLength(){
        return devStudios.size();
    }

    public void addGameToDevs(List<String> devsNames, String gameName, int gameYear){
        for (String devsName : devsNames) {
            /*for (XMLdevStudio devStudio : devStudios) {
                if (devStudio.getName().equals(devsName))
                    devStudio.addGame(gameName, gameYear);
            }*/

            Stream<XMLdevStudio> stream = devStudios.stream();

            stream.filter(x -> x.getName().equals(devsName)).forEach(x -> x.addGame(gameName, gameYear));
        }
    }
}
