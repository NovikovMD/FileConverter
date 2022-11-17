package FileConverter.Classes.XML;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XMLgamePublisher {
    private String name;
    private ArrayList<XMLdevStudio> devStudios;

    public XMLgamePublisher(String name) {
        this.name = name;
        this.devStudios = new ArrayList<XMLdevStudio>();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addDevStudio(String name, int yearOfFoundation, String URL){
        devStudios.add(new XMLdevStudio(name,yearOfFoundation, URL));
    }

    public ArrayList<XMLdevStudio> getDevStudios(){
        return devStudios;
    }

    public int returnLength(){
        return devStudios.size();
    }

    public void addGameToDevs(List<String> devsNames, String gameName, int gameYear){
        for (String devsName : devsNames) {
            /*for (XMLdevStudio devStudio : devStudios) {
                if (devStudio.getName().equals(devsName))
                    devStudio.addGame(gameName, gameYear);
            }*/

            Stream<XMLdevStudio> stream = devStudios.stream();

            stream.filter(x -> x.getName().equals(devsName)).forEach(x -> x.addGame(gameName, gameYear));
        }
    }

    public void sortByGameName(){
        Stream<XMLdevStudio> stream = devStudios.stream();

        stream.forEach(x -> x.getGames().sort(Comparator.comparing(XMLgame::getName)));
    }

    public List<XMLdevStudio> getAllStartingWith(char start){
        Stream<XMLdevStudio> stream = devStudios.stream();

        return stream.filter(x -> x.getName().charAt(0) == start).distinct().collect(Collectors.toList());
    }

    public List<XMLdevStudio> getAllSorted(){
        Stream<XMLdevStudio> stream = devStudios.stream();

        return stream.sorted(Comparator.comparing(XMLdevStudio::getName)).collect(Collectors.toList());
    }

    public void appendInAllNames(String append){
        Stream<XMLdevStudio> stream = devStudios.stream();

        stream.forEach(x -> x.setName(x.getName() + append));
    }


}
