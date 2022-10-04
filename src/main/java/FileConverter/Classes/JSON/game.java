package FileConverter.Classes.JSON;


import java.util.ArrayList;

public class game {
    private String name;
    private int year;
    private String gamePublisher;
    private ArrayList<platform> platforms;
    private ArrayList<devStudio> devStudios;

    public game(String name, int year, String gamePublisher) {
        this.name = name;
        this.year = year;
        this.gamePublisher = gamePublisher;
        this.platforms = new ArrayList<platform>();
        this.devStudios = new ArrayList<devStudio>();
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getGamePublisher() {
        return gamePublisher;
    }

    public ArrayList<platform> getPlatforms() {
        return platforms;
    }

    public ArrayList<devStudio> getDevStudios() {
        return devStudios;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setGamePublisher(String gamePublisher) {
        this.gamePublisher = gamePublisher;
    }

    public void addPlatform(String name){
        platforms.add(new platform(name));
    }
    public void addDevStudio(String name){
        devStudios.add(new devStudio(name));
    }
}
