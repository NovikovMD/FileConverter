/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */

import file_converter.classes.xml.XmlUpperClass;
import file_converter.json_to_xml.JsonToXml;
import file_converter.xml_to_json.XmlToJson;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Starter class for FileConverter
 */
public class Main {
    private static final XmlToJson xmlToJsonParser = new XmlToJson();
    /**
     * Starts converting for xml or json file depending on the input data
     *
     * @param args Required 2 parameters: 1 - path to existing file; 2 - path to new file
     */
    public static void main(String[] args) {
        String path;
        String newPath;

        if (args.length == 0) {
            Scanner inp = new Scanner(System.in);
            System.out.print("Input full path to file: ");
            path = inp.nextLine();

            System.out.print("Input full path to new file: ");
            newPath = inp.nextLine();
        } else {
            path = args[0];
            newPath = args[1];
        }


        String firstExtension = getExtension(path);
        String secondExtension = getExtension(newPath);

        if (firstExtension.equals("json") && secondExtension.equals("xml")) {
            parseJson(path, newPath);
        } else if (firstExtension.equals("xml") && secondExtension.equals("json")) {
            parseXml(path, newPath);
        } else {
            System.out.print("Wrong input");
        }
    }

    private static String getExtension(String newPath) {
        int index = newPath.lastIndexOf(".");
        return index > -1 ? newPath.substring( index + 1) : "";
    }

    private static void parseXml(String path, String newPath) {
        XmlUpperClass parsedClass;
        try {
            parsedClass = xmlToJsonParser.parseXml(path);
        } catch (ParserConfigurationException | SAXException | IOException exception) {
            System.out.println("Failed to parse xml file");
            exception.printStackTrace();
            return;
        }

        try {
            xmlToJsonParser.createJson(xmlToJsonParser.convert(parsedClass), newPath);
        } catch (IOException ioException) {
            System.out.println("Failed to create json file");
            ioException.printStackTrace();
        }
    }

    private static void parseJson(String path, String newPath) {
        try {
            JsonToXml.parseJson(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonToXml.createXML(JsonToXml.convert(), newPath);
    }
}