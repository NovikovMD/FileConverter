package FileConverter.Classes.JSON;

import java.util.ArrayList;

public class JSON {
    private ArrayList<game> games;

    public JSON() {
        this.games = new ArrayList<>();
    }

    public ArrayList<game> getGames() {
        return games;
    }

    public void addGame(String name, int year, String gamePublisher){
        games.add(new game(name,year,gamePublisher));
    }

    public int returnLength(){
        return games.size();
    }
}
