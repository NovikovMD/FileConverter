/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package fileconverter.bean.xml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class XmlDevStudio {
    private String name;
    private int yearOfFoundation;
    private String url;
    private final ArrayList<XmlGame> games= new ArrayList<>();

    public void addGame(String name, int year) {
        games.add(new XmlGame(name, year));
    }

    public int returnLength() {
        return games.size();
    }
}
