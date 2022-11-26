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

    //region methods that use streams
    public void addGameToDevs(List<String> devsNames, String gameName, int gameYear) {
        for (String devsName : devsNames) {
            Stream<XmlDevStudio> stream = devStudios.stream();

            stream.filter(x -> x.getName().equals(devsName)).forEach(x -> x.addGame(gameName, gameYear));
        }
    }

    public void sortByGameName() {
        Stream<XmlDevStudio> stream = devStudios.stream();

        stream.forEach(x -> x.getGames().sort(Comparator.comparing(XmlGame::getName)));
    }

    public List<XmlDevStudio> getAllStartingWith(char start) {
        Stream<XmlDevStudio> stream = devStudios.stream();

        return stream.filter(x -> x.getName().charAt(0) == start).distinct().collect(Collectors.toList());
    }

    public List<XmlDevStudio> getAllSorted() {
        Stream<XmlDevStudio> stream = devStudios.stream();

        return stream.sorted(Comparator.comparing(XmlDevStudio::getName)).collect(Collectors.toList());
    }

    public void appendInAllNames(String append) {
        Stream<XmlDevStudio> stream = devStudios.stream();
        stream.forEach(x -> x.setName(x.getName() + append));
    }
    //endregion

}
