/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */

import file_converter.classes.json.JsonUpperClass;
import file_converter.classes.xml.XmlUpperClass;
import file_converter.json_to_xml.JsonToXml;
import file_converter.xml_to_json.XmlToJson;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Главный класс для запуска
 */
public class Main {
    /**
     * Запускает ковертирование xml\json файлов в зависимости от входных данных
     *
     * @param args Требуется два элемента:
     *             1 - путь к существующему xml файлу;
     *             2 - путь к новому json файлу.
     */
    public static void main(String[] args) {
        String path;
        String newPath;

        final XmlToJson xmlToJsonParser = new XmlToJson();
        final JsonToXml jsonToXmlParser = new JsonToXml();

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
            parseJson(path, newPath, jsonToXmlParser);
        } else if (firstExtension.equals("xml") && secondExtension.equals("json")) {
            parseXml(path, newPath, xmlToJsonParser);
        } else {
            System.out.print("Wrong input");
        }
    }

    private static String getExtension(final String newPath) {
        int index = newPath.lastIndexOf(".");
        return index > -1 ? newPath.substring(index + 1) : "";
    }

    private static void parseXml(final String path, final String newPath, XmlToJson parser) {
        XmlUpperClass parsedClass;
        try {
            parsedClass = parser.parseXml(path);
        } catch (ParserConfigurationException | SAXException | IOException exception) {
            System.out.println("Failed to parse xml file.");
            exception.printStackTrace();
            return;
        } catch (IllegalArgumentException exception) {
            System.out.println("Wrong file path.");
            exception.printStackTrace();
            return;
        }

        try {
            parser.createJson(parser.convert(parsedClass), newPath);
        } catch (IOException ioException) {
            System.out.println("Failed to create json file.");
            ioException.printStackTrace();
        }catch (IllegalArgumentException exception){
            System.out.println("Illegal null argument.");
            exception.printStackTrace();
        }
    }

    private static void parseJson(final String path, final String newPath, JsonToXml parser) {
        JsonUpperClass jsonClass;
        try {
            jsonClass = parser.parseJson(path);
        } catch (IOException exception) {
            System.out.println("Failed to parse json file.");
            exception.printStackTrace();
            return;
        } catch (IllegalArgumentException exception) {
            System.out.println("Wrong file path.");
            exception.printStackTrace();
            return;
        }

        try {
            parser.createXML(parser.convert(jsonClass), newPath);
        } catch (FileNotFoundException exception) {
            System.out.println("File not found.");
            exception.printStackTrace();
        } catch (XMLStreamException exception) {
            System.out.println("Failed to create json file.");
            exception.printStackTrace();
        } catch (IllegalArgumentException exception){
            System.out.println("Illegal null argument.");
            exception.printStackTrace();
        }
    }
}