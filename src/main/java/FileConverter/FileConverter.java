package FileConverter;

import FileConverter.JSON_to_XML.JSON_to_XML;
import FileConverter.XML_to_JSON.XML_to_JSON;

public class FileConverter {
    public void convertToJson(String pathToXML, String pathToNewFile)  {
        try {
            XML_to_JSON.parseXML(pathToXML);
            XML_to_JSON.createJSON(XML_to_JSON.convert(), pathToNewFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void convertToXML(String pathToJSON, String pathToNewFile)  {
        try {
            JSON_to_XML.parseJSON(pathToJSON);
            JSON_to_XML.createXML(JSON_to_XML.convert(), pathToNewFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
