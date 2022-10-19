package FileConverter.Classes.XML;

import java.util.ArrayList;

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
}
