package file_converter;

import file_converter.classes.json.JsonUpperClass;
import file_converter.classes.xml.XmlUpperClass;
import file_converter.json_to_xml.JsonToXml;
import file_converter.xml_to_json.XmlToJson;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Обработчик входных данных.
 */
public class FileConverter {
    private static final Logger LOGGER = Logger.getLogger(FileConverter.class);
    private static final JsonToXml JSON_TO_XML = new JsonToXml();
    private static final XmlToJson XML_TO_JSON = new XmlToJson();

    /**
     * Проверяет корректность входных данных
     * и запускает процесс конвертирования файлов.
     * @param params Требуется два элемента:
     *               1) - путь к существующему xml\json файлу.
     *               2) - путь к новому xmk\json файлу.
     * @throws Exception если в ходе работы произошли любые ошибки
     */
    public void doParse(List<String> params) throws Exception {
        LOGGER.info("Начало работы программы");

        if (params == null) {
            LOGGER.error("Некорректный ввод данных. Завершение программы.");
            throw new Exception("Некорректный ввод данных.");
        }

        if (params.size() == 1) {
            LOGGER.error("Некорректный ввод данных. Завершение программы.");
            throw new Exception("Некорректный ввод данных.");
        }
        String path;
        String newPath;


        if (params.size() == 0) {
            LOGGER.warn("Нет входных данных. Попытка запроса у пользователя.");

            Scanner inp = new Scanner(System.in);
            System.out.print("Введите абсолютный путь к файлу: ");
            path = inp.nextLine();

            System.out.print("Введите абсолютный путь к создаваемому файлу: ");
            newPath = inp.nextLine();
        } else {
            path = params.get(0);
            newPath = params.get(1);
        }


        String firstExtension = getExtension(path);
        String secondExtension = getExtension(newPath);

        if (firstExtension.equals("json") && secondExtension.equals("xml")) {
            parseJson(path, newPath);
        } else if (firstExtension.equals("xml") && secondExtension.equals("json")) {
            parseXml(path, newPath);
        } else {
            LOGGER.error("Некорректный формат входных данных. Завершение программы.");
            throw new Exception("Некорректный формат входных данных.");
        }

        LOGGER.info("Успешное завершение работы программы");
    }

    private String getExtension(final String newPath) {
        int index = newPath.lastIndexOf(".");
        return index > -1 ? newPath.substring(index + 1) : "";
    }

    private void parseXml(final String path, final String newPath) throws Exception {
        LOGGER.info("Начало работы парсинга XML");

        final XmlUpperClass parsedClass;
        try {
            parsedClass = XML_TO_JSON.parseXml(path);
        } catch (ParserConfigurationException | SAXException | IOException exception) {
            LOGGER.error("Не удалось считать файл XML.", exception);
            throw new Exception("Не удалось считать файл XML.", exception);
        } catch (IllegalArgumentException exception) {
            LOGGER.error("Неверный путь к XML файлу.", exception);
            throw new Exception("Неверный путь к XML файлу.", exception);
        }
        LOGGER.info("Успешное завершение парсинга XML.");

        try {
            XML_TO_JSON.createJson(XML_TO_JSON.convert(parsedClass), newPath);
        } catch (IOException ioException) {
            LOGGER.error("Не удалось создать JSON файл.", ioException);
            throw new Exception("Не удалось создать JSON файл.", ioException);
        } catch (IllegalArgumentException exception) {
            LOGGER.fatal("Не удалось конвертировать XML классы в JSON.", exception);
            throw new Exception("Не удалось конвертировать XML классы в JSON.", exception);
        }

        LOGGER.info("Успешно создан JSON файл.");
    }

    private void parseJson(final String path, final String newPath) throws Exception {
        final JsonUpperClass jsonClass;
        try {
            jsonClass = JSON_TO_XML.parseJson(path);
        } catch (IOException exception) {
            LOGGER.error("Не удалось считать файл JSON.", exception);
            throw new Exception("Не удалось считать файл JSON.", exception);
        } catch (IllegalArgumentException exception) {
            LOGGER.error("Неверный путь к JSON файлу.", exception);
            throw new Exception("Неверный путь к JSON файлу.", exception);
        }

        try {
            JSON_TO_XML.createXML(JSON_TO_XML.convert(jsonClass), newPath);
        } catch (FileNotFoundException exception) {
            LOGGER.error("Введен неверный путь к файлу XML.", exception);
            throw new Exception("Введен неверный путь к файлу XML.", exception);
        } catch (XMLStreamException exception) {
            LOGGER.error("Не удалось создать XML файл.", exception);
            throw new Exception("Не удалось создать XML файл.", exception);
        } catch (IllegalArgumentException exception) {
            LOGGER.error("Не удалось конвертировать JSON классы в XML.", exception);
            throw new Exception("Не удалось конвертировать JSON классы в XML.", exception);
        }
    }
}
