/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package FileConverter.Classes.JSON;

import java.util.ArrayList;

/**
 * First node in Json file.
 * Contains a list of games.
 *
 * @author Novikov Matthew
 */
public class JsonUpperClass {
    private final ArrayList<JsonGame> games;

    public JsonUpperClass() {
        this.games = new ArrayList<>();
    }

    public ArrayList<JsonGame> getGames() {
        return games;
    }

    public void addGame(String name, int year, String gamePublisher) {
        games.add(new JsonGame(name, year, gamePublisher));
    }

    public int returnLength() {
        return games.size();
    }

    public JsonGame returnLastGame() {
        if (games.size() > 0) {
            return games.get(games.size() - 1);
        } else {
            return null;
        }
    }
}
