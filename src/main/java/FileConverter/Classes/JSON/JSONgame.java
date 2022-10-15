package FileConverter.Classes.JSON;


import java.util.ArrayList;

public class JSONgame {
    private String name;
    private int year;
    private String gamePublisher;
    private ArrayList<JSONplatform> platforms;
    private ArrayList<JSONdevStudio> devStudios;

    public JSONgame(String name, int year, String gamePublisher) {
        this.name = name;
        this.year = year;
        this.gamePublisher = gamePublisher;
        this.platforms = new ArrayList<JSONplatform>();
        this.devStudios = new ArrayList<JSONdevStudio>();
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

    public ArrayList<JSONplatform> getPlatforms() {
        return platforms;
    }

    public ArrayList<JSONdevStudio> getDevStudios() {
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
        platforms.add(new JSONplatform(name));
    }
    public void addDevStudio(String name, int yearOfFoundation, String URL){
        devStudios.add(new JSONdevStudio(name,yearOfFoundation,URL));
    }

    public JSONdevStudio getLastDevStudio(){
        return devStudios.get(devStudios.size()-1);
    }
}
