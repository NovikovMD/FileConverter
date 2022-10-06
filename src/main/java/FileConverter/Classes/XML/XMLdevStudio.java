package FileConverter.Classes.XML;

import java.util.ArrayList;

public class XMLdevStudio {

    private String name;
    private int yearOfFoundation;
    private String URL;
    private ArrayList<XMLgame> games;

    public XMLdevStudio(String name, int yearOfFoundation, String URL) {
        this.name = name;
        this.yearOfFoundation = yearOfFoundation;
        this.URL = URL;
        games = new ArrayList<XMLgame>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setYearOfFoundation(int yearOfFoundation) {
        this.yearOfFoundation = yearOfFoundation;
    }

    public int getYearOfFoundation() {
        return yearOfFoundation;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURL(){
        return URL;
    }

    public ArrayList<XMLgame> getGames() {
        return games;
    }

    public void addGame(String name, int year){
        games.add(new XMLgame(name, year));
    }

    public int getLength(){
        return games.size();
    }
}
