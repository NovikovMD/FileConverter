package FileConverter.XML_to_JSON;

import FileConverter.Classes.XML.XML;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

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
            else if (qName.equals("developer_studio")){
                String name = attributes.getValue("name");
                Integer year = Integer.parseInt(attributes.getValue("year_of_foundation"));
                String URL = attributes.getValue("URL");
                gameIndustry.getPublishers().get(gameIndustry.getLength()-1).addDevStudio(name,year,URL);
            }
            else if (qName.equals("game")){
                String name = attributes.getValue("name");
                int year = Integer.parseInt(attributes.getValue("year"));
                gameIndustry.getPublishers().get(gameIndustry.getLength()-1).getDevStudios()
                        .get(gameIndustry.getPublishers().get(gameIndustry.getLength()-1).getLength()-1).addGame(name,year);
            }
            else if (qName.equals("platform")){
                String name = attributes.getValue("name");
                gameIndustry.getPublishers().get(gameIndustry.getLength()-1).getDevStudios()
                        .get(gameIndustry.getPublishers().get(gameIndustry.getLength()-1).getLength()-1).getGames()
                        .get(gameIndustry.getPublishers().get(gameIndustry.getLength()-1).getDevStudios().
                                get(gameIndustry.getPublishers().get(gameIndustry.getLength()-1).getLength()-1).getLength()-1).addPlatform(name);
            }
        }
    }

}
