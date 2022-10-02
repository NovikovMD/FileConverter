package FileConverter.XML_to_JSON;


import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XML_to_JSON {

    public static Document parseXML(String path) throws ParserConfigurationException, IOException, SAXException {
        // Make builder which can parse XML file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //Return parsed document
        return builder.parse(new File(path));
    }

}
