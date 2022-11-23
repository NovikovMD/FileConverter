/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package FileConverter;

import FileConverter.JSON_to_XML.JsonToXml;
import FileConverter.XML_to_JSON.XmlToJson;

import java.io.IOException;

/**
 * Converter XML\Json files in both directions
 * @author Novikov Matthew
 * @version 1.0
 */
public class FileConverter {

    /**
     * Starts parse for XML file, then creates Json file
     * @param pathToXML absolute path to existing XML file
     * @param pathToNewFile absolute path to new Json file
     */
    public void convertToJson(String pathToXML, String pathToNewFile) {
        XmlToJson.parseXml(pathToXML);
        XmlToJson.createJson(XmlToJson.convert(), pathToNewFile);
    }

    /**
     * Starts parse for Json file, then creates XML file
     * @param pathToJSON absolute path to existing Json file
     * @param pathToNewFile absolute path to new XML file
     */
    public void convertToXML(String pathToJSON, String pathToNewFile) {
        try {
            JsonToXml.parseJson(pathToJSON);
        } catch (IOException e) {
            throw new RuntimeException("Method parseJson caused exception. Failed to parse file:\n" + e);
        }

        JsonToXml.createXML(JsonToXml.convert(), pathToNewFile);
    }

}
