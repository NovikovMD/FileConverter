import FileConverter.XML_to_JSON.XML_to_JSON;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Objects;

public class Test_XMLtoJSON {

    @Test
    public void tryGetSomething() throws ParserConfigurationException, IOException, SAXException {
        Assert.assertNotNull(XML_to_JSON.parseXML("src/test/resources/TestInput.xml"));
    }

    @Test
    public void tryParseDeveloper() throws ParserConfigurationException, IOException, SAXException {

        Document doc = XML_to_JSON.parseXML("src/test/resources/TestInput.xml");

        //get list of "game_publisher" objects
        NodeList publishers = doc.getDocumentElement().getElementsByTagName("developer_studio");
        Node publisher = publishers.item(0);

        // Get attributes of each element
        NamedNodeMap attributes = publisher.getAttributes();

        Assert.assertEquals("Rockstar Toronto", attributes.getNamedItem("name").getNodeValue());

    }
    @Test
    public void tryParseGames() throws ParserConfigurationException, IOException, SAXException {

        Document doc = XML_to_JSON.parseXML("src/test/resources/TestInput.xml");

        //get list of "game_publisher" objects
        NodeList games = doc.getDocumentElement().getElementsByTagName("game");

        Node publisher = games.item(0);
        // Get attributes of each element
        NamedNodeMap attributes = publisher.getAttributes();
        Assert.assertEquals("The Warriors", attributes.getNamedItem("name").getNodeValue());


        publisher = games.item(1);
        // Get attributes of each element
        attributes = publisher.getAttributes();
        Assert.assertEquals("Manhunt 2", attributes.getNamedItem("name").getNodeValue());

    }

    @Test
    public void tryParsePlatforms() throws ParserConfigurationException, IOException, SAXException {
        //try to get platforms from only "Manhunt 2" game


        Document doc = XML_to_JSON.parseXML("src/test/resources/TestInput.xml");

        //get list of "game_publisher" objects
        NodeList games = doc.getElementsByTagName("game");
        //find "Manhunt 2"
        for (int i=0;i<games.getLength();i++) {
            Node game = games.item(i);

            if (Objects.equals(game.getAttributes().getNamedItem("name").getNodeValue(), "Manhunt 2")) {
                //go down to children
                NodeList childNodes = game.getChildNodes();

                //find "platforms"
                for (int j=0;j<childNodes.getLength();j++){
                    if (childNodes.item(i).getNodeName().equals("platforms")) {
                        //go down to children
                        NodeList platforms = childNodes.item(i).getChildNodes();

                        //compare all platforms
                        Node platform = platforms.item(0);
                        Assert.assertEquals("Microsoft Windows", platform.getNodeValue());
                        platform = platforms.item(1);
                        Assert.assertEquals("PlayStation 2", platform.getNodeValue());
                        platform = platforms.item(2);
                        Assert.assertEquals("PlayStation Portable", platform.getNodeValue());
                        platform = platforms.item(3);
                        Assert.assertEquals("Wii", platform.getNodeValue());

                    }

                }

            }
        }

    }

}
