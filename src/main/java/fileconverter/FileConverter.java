package fileconverter;

import fileconverter.bean.InputBean;
import fileconverter.converters.*;
import fileconverter.readers.json.JacksonReader;
import fileconverter.readers.Reader;
import fileconverter.readers.xml.SaxReader;
import fileconverter.writers.json.JacksonWriter;
import fileconverter.writers.xml.StaxWriter;
import fileconverter.writers.Writer;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import org.apache.logging.log4j.Level;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * Запускает парсинг файлов
 */
@Log4j2
public class FileConverter {
    Reader reader;
    Converter converter;
    Writer writer;

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
        throws XMLStreamException, IOException, ParserConfigurationException, SAXException, JAXBException {
        if (log.isEnabled(Level.INFO))
            log.log(Level.INFO, "Начало работы программы");

        setupConfig(bean.getExistingFileExtension());

        try {
            writer.write(
                converter.convert(
                    reader.parse(bean.getExistingFile())),
                bean.getNewFile());
        } catch (XMLStreamException | IOException | ParserConfigurationException |
                 JAXBException | SAXException exception) {
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

    private void setupConfig(String extension) {
        switch (extension) {
            case "json" -> {
                reader = new JacksonReader();
                converter = new JsonToXml();
                writer = new StaxWriter();
            }
            case "xml" -> {
                reader = new SaxReader();
                converter = new XmlToJson();
                writer = new JacksonWriter();
            }
        }
    }
}
