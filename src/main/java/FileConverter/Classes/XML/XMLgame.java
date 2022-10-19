package FileConverter.Classes.XML;

import java.util.ArrayList;

public class XMLgame {
    private String name;
    private int year;
    private ArrayList<XMLplatform> platforms;

    public XMLgame(String name, int year) {
        this.name = name;
        this.year = year;
        this.platforms = new ArrayList<>();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear(){
        return year;
    }

    public ArrayList<XMLplatform> getPlatforms() {
        return platforms;
    }

    public void addPlatform(String name){
        platforms.add(new XMLplatform(name));
    }

    public int returnLength(){
        return platforms.size();
    }
}
