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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Second node in XML file.
 * Contains its name and a list of developer studios managed by.
 *
 * @author Novikov Matthew
 */
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
