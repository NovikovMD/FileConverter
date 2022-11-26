/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package file_converter.classes.xml;

import java.util.ArrayList;

/**
 * Third node in XML file.
 * Contains its name, year of foundation, url and a list of games that it has developed.
 *
 * @author Novikov Matthew
 */
public class XmlDevStudio {

    private String name;
    private int yearOfFoundation;
    private String url;
    private final ArrayList<XmlGame> games;

    public XmlDevStudio(String name, int yearOfFoundation, String url) {
        this.name = name;
        this.yearOfFoundation = yearOfFoundation;
        this.url = url;
        games = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getYearOfFoundation() {
        return yearOfFoundation;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<XmlGame> getGames() {
        return games;
    }

    public void addGame(String name, int year) {
        games.add(new XmlGame(name, year));
    }

    public int returnLength() {
        return games.size();
    }
}
