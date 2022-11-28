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
import logger.Logger;
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
        Logger.getInstance().info("Начало работы программы");

        if (args == null) {
            Logger.getInstance().error("Пустой ввод данных. Завершение программы.");
            return;
        }

        if (args.length == 1) {
            Logger.getInstance().error("Некорректный ввод данных. Завершение программы.");
            return;
        }
        String path;
        String newPath;


        if (args.length == 0) {
            Logger.getInstance().warn("Нет входных данных. Попытка запроса у пользователя.");

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
            System.err.println("Wrong input");
            Logger.getInstance().fatal("Некорректный формат входных данных. Завершение программы.");
        }

        Logger.getInstance().info("Успешное завершение работы программы");
    }

    private static String getExtension(final String newPath) {
        int index = newPath.lastIndexOf(".");
        return index > -1 ? newPath.substring(index + 1) : "";
    }

    private static void parseXml(final String path, final String newPath) {
        Logger.getInstance().info("Начало работы парсинга XML");

        final XmlToJson parser = new XmlToJson();
        XmlUpperClass parsedClass;
        try {
            parsedClass = parser.parseXml(path);
        } catch (ParserConfigurationException | SAXException | IOException exception) {
            Logger.getInstance().fatal("Не удалось считать файл XML.", exception);
            System.err.println("Failed to parse xml file.");
            exception.printStackTrace();
            return;
        } catch (IllegalArgumentException exception) {
            Logger.getInstance().error("Неверный путь к XML файлу.", exception);
            System.err.println("Wrong file path.");
            exception.printStackTrace();
            return;
        }
        Logger.getInstance().info("Успешное завершение парсинга XML.");

        try {
            parser.createJson(parser.convert(parsedClass), newPath);
        } catch (IOException ioException) {
            Logger.getInstance().fatal("Не удалось создать JSON файл.", ioException);
            System.err.println("Failed to create json file.");
            ioException.printStackTrace();
            return;
        } catch (IllegalArgumentException exception) {
            Logger.getInstance().fatal("Не удалось конвертировать XML классы в JSON.", exception);
            System.err.println("Failed to convert data.");
            exception.printStackTrace();
            return;
        }

        Logger.getInstance().info("Успешно создан JSON файл.");
    }

    private static void parseJson(final String path, final String newPath) {
        Logger.getInstance().info("Начало работы парсинга JSON");

        final JsonToXml parser = new JsonToXml();
        JsonUpperClass jsonClass;
        try {
            jsonClass = parser.parseJson(path);
        } catch (IOException exception) {
            Logger.getInstance().fatal("Не удалось считать файл JSON.", exception);
            System.err.println("Failed to parse json file.");
            exception.printStackTrace();
            return;
        } catch (IllegalArgumentException exception) {
            Logger.getInstance().error("Неверный путь к JSON файлу.", exception);
            System.err.println("Wrong file path.");
            exception.printStackTrace();
            return;
        }
        Logger.getInstance().info("Успешное завершение парсинга JSON.");

        try {
            parser.createXML(parser.convert(jsonClass), newPath);
        } catch (FileNotFoundException exception) {
            Logger.getInstance().error("Введен неверный путь к файлу XML.", exception);
            System.err.println("Incorrect file path.");
            exception.printStackTrace();
            return;
        } catch (XMLStreamException exception) {
            Logger.getInstance().fatal("Не удалось создать XML файл.", exception);
            System.err.println("Failed to create xml file.");
            exception.printStackTrace();
            return;
        } catch (IllegalArgumentException exception) {
            Logger.getInstance().fatal("Не удалось конвертировать JSON классы в XML.", exception);
            System.err.println("Failed to convert data.");
            exception.printStackTrace();
            return;
        }

        Logger.getInstance().info("Успешно создан XML файл.");
    }
}