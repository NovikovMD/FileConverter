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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class JsonGame {
    private String name;
    private int year;
    private String gamePublisher;
    private final ArrayList<JsonPlatform> platforms = new ArrayList<>();
    private final ArrayList<JsonDevStudio> devStudios = new ArrayList<>();

    public void addPlatform(String name) {
        platforms.add(new JsonPlatform(name));
    }

    public void addDevStudio(String name, int yearOfFoundation, String url) {
        devStudios.add(new JsonDevStudio(name, yearOfFoundation, url));
    }

    public JsonDevStudio returnLastDevStudio() {
        return devStudios.get(devStudios.size() - 1);
    }
}
