package FileConverter.Classes.JSON;

import java.util.ArrayList;

public class JSON {
    private ArrayList<JSONgame> games;

    public JSON() {
        this.games = new ArrayList<>();
    }

    public ArrayList<JSONgame> getGames() {
        return games;
    }

    public void addGame(String name, int year, String gamePublisher){
        games.add(new JSONgame(name,year,gamePublisher));
    }

    public int returnLength(){
        return games.size();
    }

    public JSONgame returnLastGame() {
        if (games.size() > 0)
            return games.get(games.size() - 1);
        else
            return null;
    }
}
