package FileConverter.Classes;

import java.util.ArrayList;

public class devStudio {

    private String name;
    private int yearOfFoundation;
    private String URL;
    private ArrayList<game> games;

    public devStudio(String name, int yearOfFoundation, String URL) {
        this.name = name;
        this.yearOfFoundation = yearOfFoundation;
        this.URL = URL;
        games = new ArrayList<game>();
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

    public ArrayList<game> getGames() {
        return games;
    }

    public void addGame(String name, int year){
        games.add(new game(name, year));
    }
}
