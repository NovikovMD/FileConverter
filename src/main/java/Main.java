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
    public static void main(String[] args) throws Exception {
        Logger.getInstance().info("Начало работы программы");

        if (args == null) {
            Logger.getInstance().error("Пустой ввод данных. Завершение программы.");
            throw new Exception("Некорректный ввод данных.");
        }

        if (args.length == 1) {
            Logger.getInstance().error("Некорректный ввод данных. Завершение программы.");
            throw new Exception("Некорректный ввод данных.");
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
            Logger.getInstance().fatal("Некорректный формат входных данных. Завершение программы.");
            throw new Exception("Некорректный формат входных данных.");
        }

        Logger.getInstance().info("Успешное завершение работы программы");
    }

    private static String getExtension(final String newPath) {
        int index = newPath.lastIndexOf(".");
        return index > -1 ? newPath.substring(index + 1) : "";
    }

    private static void parseXml(final String path, final String newPath) throws Exception {
        Logger.getInstance().info("Начало работы парсинга XML");

        final XmlToJson parser = new XmlToJson();
        final XmlUpperClass parsedClass;
        try {
            parsedClass = parser.parseXml(path);
        } catch (ParserConfigurationException | SAXException | IOException exception) {
            Logger.getInstance().error("Не удалось считать файл XML.", exception);
            throw new Exception("Не удалось считать файл XML.", exception);
        } catch (IllegalArgumentException exception) {
            Logger.getInstance().error("Неверный путь к XML файлу.", exception);
            throw new Exception("Неверный путь к XML файлу.", exception);
        }
        Logger.getInstance().info("Успешное завершение парсинга XML.");

        try {
            parser.createJson(parser.convert(parsedClass), newPath);
        } catch (IOException ioException) {
            Logger.getInstance().error("Не удалось создать JSON файл.", ioException);
            throw new Exception("Не удалось создать JSON файл.", ioException);
        } catch (IllegalArgumentException exception) {
            Logger.getInstance().fatal("Не удалось конвертировать XML классы в JSON.", exception);
            throw new Exception("Не удалось конвертировать XML классы в JSON.", exception);
        }

        Logger.getInstance().info("Успешно создан JSON файл.");
    }

    private static void parseJson(final String path, final String newPath) throws Exception {
        Logger.getInstance().info("Начало работы парсинга JSON");

        final JsonToXml parser = new JsonToXml();
        final JsonUpperClass jsonClass;
        try {
            jsonClass = parser.parseJson(path);
        } catch (IOException exception) {
            Logger.getInstance().error("Не удалось считать файл JSON.", exception);
            throw new Exception("Не удалось считать файл JSON.", exception);
        } catch (IllegalArgumentException exception) {
            Logger.getInstance().error("Неверный путь к JSON файлу.", exception);
            throw new Exception("Неверный путь к JSON файлу.", exception);
        }
        Logger.getInstance().info("Успешное завершение парсинга JSON.");

        try {
            parser.createXML(parser.convert(jsonClass), newPath);
        } catch (FileNotFoundException exception) {
            Logger.getInstance().error("Введен неверный путь к файлу XML.", exception);
            throw new Exception("Введен неверный путь к файлу XML.", exception);
        } catch (XMLStreamException exception) {
            Logger.getInstance().error("Не удалось создать XML файл.", exception);
            throw new Exception("Не удалось создать XML файл.", exception);
        } catch (IllegalArgumentException exception) {
            Logger.getInstance().error("Не удалось конвертировать JSON классы в XML.", exception);
            throw new Exception("Не удалось конвертировать JSON классы в XML.", exception);
        }

        Logger.getInstance().info("Успешно создан XML файл.");
    }
}