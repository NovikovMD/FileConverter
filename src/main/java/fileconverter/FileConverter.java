package fileconverter;

import fileconverter.bean.InputBean;
import fileconverter.bean.json.JsonUpperClass;
import fileconverter.bean.xml.XmlUpperClass;
import fileconverter.jsontoxml.JsonToXml;
import fileconverter.xmltojson.XmlToJson;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Обработчик входных данных.
 */
@Log4j
public class FileConverter {
    private static final JsonToXml JSON_TO_XML = new JsonToXml();
    private static final XmlToJson XML_TO_JSON = new XmlToJson();

    /**
     * Проверяет корректность входных данных
     * и запускает процесс конвертирования файлов.
     *
     * @param bean pathToExistingFile - путь к существующему xml\json файлу.
     *             pathToNewFile - путь к новому xml\json файлу.
     * @throws Exception если в ходе работы произошли любые ошибки.
     */
    public void doParse(@NonNull InputBean bean) throws Exception {
        log.info("Начало работы программы");

        validation(bean);

        switch (getExtension(bean.getPathToExistingFile())){
            case "json" -> parseJson(bean.getPathToExistingFile(), bean.getPathToNewFile());
            case "xml" -> parseXml(bean.getPathToExistingFile(), bean.getPathToNewFile());
        }

        log.info("Успешное завершение работы программы");
    }

    private void validation(InputBean bean) throws Exception {
        if (bean.getPathToExistingFile() == null ||
            bean.getPathToNewFile() == null) {
            log.error("Некорректный ввод данных. Завершение программы.");
            throw new Exception("Некорректный ввод данных.");
        }

        if (!(getExtension(bean.getPathToExistingFile()).equals("json") &&
                getExtension(bean.getPathToNewFile()).equals("xml")) &&
            !(getExtension(bean.getPathToExistingFile()).equals("xml") &&
                getExtension(bean.getPathToNewFile()).equals("json"))) {
            log.error("Некорректный формат входных данных. Завершение программы.");
            throw new Exception("Некорректный формат входных данных.");
        }
    }

    private String getExtension(final String newPath) {
        return newPath.lastIndexOf(".") > -1 ? newPath.substring(newPath.lastIndexOf(".") + 1) : "";
    }

    private void parseXml(final String path, final String newPath) throws Exception {
        log.info("Начало работы парсинга XML");

        final XmlUpperClass parsedClass;
        try {
            parsedClass = XML_TO_JSON.parseXml(path);
        } catch (ParserConfigurationException | SAXException | IOException exception) {
            log.error("Не удалось считать файл XML.", exception);
            throw new Exception("Не удалось считать файл XML.", exception);
        } catch (IllegalArgumentException exception) {
            log.error("Неверный путь к XML файлу.", exception);
            throw new Exception("Неверный путь к XML файлу.", exception);
        }
        log.info("Успешное завершение парсинга XML.");

        try {
            XML_TO_JSON.createJson(XML_TO_JSON.convert(parsedClass), newPath);
        } catch (IOException ioException) {
            log.error("Не удалось создать JSON файл.", ioException);
            throw new Exception("Не удалось создать JSON файл.", ioException);
        } catch (IllegalArgumentException exception) {
            log.fatal("Не удалось конвертировать XML классы в JSON.", exception);
            throw new Exception("Не удалось конвертировать XML классы в JSON.", exception);
        }

        log.info("Успешно создан JSON файл.");
    }

    private void parseJson(final String path, final String newPath) throws Exception {
        final JsonUpperClass jsonClass;
        try {
            jsonClass = JSON_TO_XML.parseJson(path);
        } catch (IOException exception) {
            log.error("Не удалось считать файл JSON.", exception);
            throw new Exception("Не удалось считать файл JSON.", exception);
        } catch (IllegalArgumentException exception) {
            log.error("Неверный путь к JSON файлу.", exception);
            throw new Exception("Неверный путь к JSON файлу.", exception);
        }

        try {
            JSON_TO_XML.createXML(JSON_TO_XML.convert(jsonClass), newPath);
        } catch (FileNotFoundException exception) {
            log.error("Введен неверный путь к файлу XML.", exception);
            throw new Exception("Введен неверный путь к файлу XML.", exception);
        } catch (XMLStreamException exception) {
            log.error("Не удалось создать XML файл.", exception);
            throw new Exception("Не удалось создать XML файл.", exception);
        } catch (IllegalArgumentException exception) {
            log.error("Не удалось конвертировать JSON классы в XML.", exception);
            throw new Exception("Не удалось конвертировать JSON классы в XML.", exception);
        }
    }
}
