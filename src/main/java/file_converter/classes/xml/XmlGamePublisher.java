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

public class XmlGamePublisher {
    private String name;
    private final ArrayList<XmlDevStudio> devStudios;

    public XmlGamePublisher(String name) {
        this.name = name;
        this.devStudios = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addDevStudio(String name, int yearOfFoundation, String url) {
        devStudios.add(new XmlDevStudio(name, yearOfFoundation, url));
    }

    public ArrayList<XmlDevStudio> getDevStudios() {
        return devStudios;
    }

    public int returnLength() {
        return devStudios.size();
    }
}
