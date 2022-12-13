package fileconverter;

import fileconverter.bean.InputBean;
import fileconverter.converters.Converter;
import fileconverter.converters.JsonToXml;
import fileconverter.converters.XmlToJson;
import fileconverter.readers.Reader;
import fileconverter.readers.json.JacksonReader;
import fileconverter.readers.xml.SaxReader;
import fileconverter.writers.Writer;
import fileconverter.writers.json.JacksonWriter;
import fileconverter.writers.xml.StaxWriter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
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
     * @throws IOException                  при любой IO ошибке.
     */
    public void doParse(@NonNull final InputBean bean)
        throws IOException, XMLStreamException, JAXBException, ParserConfigurationException, SAXException {
        if (log.isInfoEnabled())
            log.info("Начало работы программы");

        setupConfig(bean.getExistingFileExtension());

        startParsing(bean);

        if (log.isInfoEnabled())
            log.info("Успешное завершение работы программы");
    }

    private void startParsing(InputBean bean)
        throws IOException, JAXBException, ParserConfigurationException, SAXException, XMLStreamException {
        try {
            writer.write(
                converter.convert(
                    reader.parse(bean.getExistingFile())),
                bean.getNewFile());
        }finally {
            bean.getExistingFile().close();
            bean.getNewFile().close();
        }
    }

    private void setupConfig(final String extension) {
        switch (extension) {
            case "json" -> {
                if (reader == null || reader.getClass() != JacksonReader.class) {
                    reader = new JacksonReader();
                    converter = new JsonToXml();
                    writer = new StaxWriter();
                }
            }
            case "xml" -> {
                if (reader == null || reader.getClass() != SaxReader.class) {
                    reader = new SaxReader();
                    converter = new XmlToJson();
                    writer = new JacksonWriter();
                }
            }
            default -> throw new IllegalArgumentException("Некорректный тип данных");
        }
    }
}
