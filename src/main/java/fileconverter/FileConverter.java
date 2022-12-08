package fileconverter;

import fileconverter.bean.InputBean;
import fileconverter.jsontoxml.JsonToXml;
import fileconverter.xmltojson.XmlToJson;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import org.apache.logging.log4j.Level;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * Запускает парсинг файлов
 */
@Log4j2
public class FileConverter {
    private static final JsonToXml JSON_TO_XML = new JsonToXml();
    private static final XmlToJson XML_TO_JSON = new XmlToJson();

    /**
     * Запускает процесс конвертирования файлов в зависимости от входных данных.
     *
     * @param bean pathToExistingFile - путь к существующему xml\json файлу.
     *             pathToNewFile - путь к новому xml\json файлу.
     * @throws XMLStreamException           при ошибке заполнения файла.
     * @throws IOException                  при любой IO ошибке.
     * @throws ParserConfigurationException при ошибке создания SAX парсера.
     * @throws SAXException                 при ошибке работы SAX парсера.
     */
    public void doParse(@NonNull InputBean bean)
        throws XMLStreamException, IOException, ParserConfigurationException, SAXException {
        if (log.isEnabled(Level.INFO))
            log.log(Level.INFO, "Начало работы программы");

        try {
            switch (bean.getExistingFileExtension()) {
                case "json" -> JSON_TO_XML.createXML(
                    JSON_TO_XML.convert(
                        JSON_TO_XML.parseJson(bean.getExistingFile())), bean.getNewFile());
                case "xml" -> XML_TO_JSON.createJson(
                    XML_TO_JSON.convert(
                        XML_TO_JSON.parseXml(bean.getExistingFile())), bean.getNewFile());
            }
        } catch (XMLStreamException | IOException | ParserConfigurationException | SAXException exception) {
            if (log.isEnabled(Level.ERROR))
                log.log(Level.ERROR, "Произошла ошибка во время выполнения.", exception);
            throw exception;
        } finally {
            bean.getExistingFile().close();
            bean.getNewFile().close();
        }

        if (log.isEnabled(Level.INFO))
            log.log(Level.INFO, "Успешное завершение работы программы");
    }
}
