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

public class XmlGame {
    private String name;
    private int year;
    private final ArrayList<XmlPlatform> platforms;

    public XmlGame(String name, int year) {
        this.name = name;
        this.year = year;
        this.platforms = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<XmlPlatform> getPlatforms() {
        return platforms;
    }

    public void addPlatform(String name) {
        platforms.add(new XmlPlatform(name));
    }

    public int returnLength() {
        return platforms.size();
    }
}
