package FileConverter.Classes;

import java.util.ArrayList;

public class XML {
    private ArrayList<gamePublisher> publishers;

    public XML(){
        publishers = new ArrayList<gamePublisher>();
    }

    public void addPublisher(String name){
        publishers.add(new gamePublisher(name));
    }

    public ArrayList<gamePublisher> getPublishers(){
        return publishers;
    }
}
