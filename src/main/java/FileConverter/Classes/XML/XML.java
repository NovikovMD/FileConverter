package FileConverter.Classes.XML;

import java.util.ArrayList;

public class XML {
    private ArrayList<XMLgamePublisher> publishers;

    public XML(){
        publishers = new ArrayList<XMLgamePublisher>();
    }

    public void addPublisher(String name){
        publishers.add(new XMLgamePublisher(name));
    }

    public ArrayList<XMLgamePublisher> getPublishers(){
        return publishers;
    }

    public int getLength(){
        return publishers.size();
    }
}
