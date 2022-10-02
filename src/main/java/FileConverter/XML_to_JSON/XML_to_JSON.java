package FileConverter.XML_to_JSON;

import FileConverter.Classes.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Flow;

public class XML_to_JSON extends DefaultHandler {
    private static XML gameIndustry = new XML();

    public static XML parseXML(String path) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        XMLHandler handler = new XMLHandler();
        parser.parse(new File(path), handler);
        return gameIndustry;
    }





    private static class XMLHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("game_publisher")) {
                String name = attributes.getValue("name");
                gameIndustry.addPublisher(name);
            }
        }
    }

}
