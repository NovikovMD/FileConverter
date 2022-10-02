package FileConverter.Classes;

import java.util.ArrayList;

public class gamePublisher {
    private String name;
    private ArrayList<devStudio> devStudios;

    public gamePublisher(String name) {
        this.name = name;
        this.devStudios = new ArrayList<devStudio>();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addDevStudio(String name, int yearOfFoundation, String URL){
        devStudios.add(new devStudio(name,yearOfFoundation, URL));
    }

    public ArrayList<devStudio> getDevStudios(){
        return devStudios;
    }
}
