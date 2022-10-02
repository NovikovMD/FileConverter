import FileConverter.Classes.*;
import FileConverter.XML_to_JSON.XML_to_JSON;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class testXMLtoJSON {

    @Test
    public void tryParse1lvl() throws ParserConfigurationException, IOException, SAXException {
        gamePublisher publisher = new gamePublisher("Rockstar");
        Assert.assertEquals(XML_to_JSON.parseXML("src/test/resources/TestInput.xml").getPublishers().get(0).getName(),
                publisher.getName());
    }
}
