package FileConverter.Classes;

import java.util.ArrayList;

public class game {
    private String name;
    private int year;
    private ArrayList<platform> platforms;

    public game(String name, int year) {
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

    public ArrayList<platform> getPlatforms() {
        return platforms;
    }

    public void addPlatform(String name){
        platforms.add(new platform(name));
    }

    public int getLength(){
        return platforms.size();
    }
}
