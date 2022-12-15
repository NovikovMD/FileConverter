/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package fileconverter.bean.json;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class JsonUpper {
    @SerializedName("Игры")
    private final ArrayList<JsonGame> games = new ArrayList<>();

    public void addGame(String name, int year, String gamePublisher) {
        games.add(new JsonGame(name, year, gamePublisher));
    }

    public int returnLength() {
        return games.size();
    }

    public JsonGame returnLastGame() {
        if (games.size() > 0) {
            return games.get(games.size() - 1);
        }

        return null;
    }
}
