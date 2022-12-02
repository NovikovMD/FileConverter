package fileconverter;

import fileconverter.bean.InputBean;
import fileconverter.jsontoxml.JsonToXml;
import fileconverter.xmltojson.XmlToJson;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
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

        switch (FilenameUtils.getExtension(bean.getPathToExistingFile())) {
            case "json" -> {
                try {
                    JSON_TO_XML.createXML(
                        JSON_TO_XML.convert(
                            JSON_TO_XML.parseJson(bean.getPathToExistingFile())), bean.getPathToNewFile());
                } catch (IOException | IllegalArgumentException | XMLStreamException exception) {
                    log.error(exception);
                    throw exception;
                }
            }
            case "xml" -> {
                try {
                    XML_TO_JSON.createJson(
                        XML_TO_JSON.convert(
                            XML_TO_JSON.parseXml(bean.getPathToExistingFile())), bean.getPathToNewFile());
                } catch (IOException | ParserConfigurationException | SAXException exception) {
                    log.error(exception);
                    throw exception;
                }
            }
        }

        log.info("Успешное завершение работы программы");
    }

    private void validation(InputBean bean) throws Exception {
        if (bean.getPathToExistingFile() == null ||
            bean.getPathToNewFile() == null) {
            log.error("Некорректный ввод данных. Завершение программы.");
            throw new Exception("Некорректный ввод данных.");
        }

        if (!(FilenameUtils.getExtension(bean.getPathToExistingFile()).equals("json") &&
            FilenameUtils.getExtension(bean.getPathToNewFile()).equals("xml")) &&
            !(FilenameUtils.getExtension(bean.getPathToExistingFile()).equals("xml") &&
                FilenameUtils.getExtension(bean.getPathToNewFile()).equals("json"))) {
            log.error("Некорректный формат входных данных. Завершение программы.");
            throw new Exception("Некорректный формат входных данных.");
        }
    }
}
