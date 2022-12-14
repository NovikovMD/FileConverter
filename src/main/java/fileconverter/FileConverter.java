package fileconverter;

import fileconverter.bean.InputBean;
import fileconverter.factory.FileConverterFactory;
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
    /**
     * Запускает процесс конвертирования файлов в зависимости от входных данных.
     *
     * @param bean pathToExistingFile - путь к существующему xml\json файлу.
     *             pathToNewFile - путь к новому xml\json файлу.
     * @throws JAXBException                при ошибке Jaxb парсера.
     * @throws XMLStreamException           при ошибке заполнения файла.
     * @throws ParserConfigurationException при ошибке создания SAX парсера.
     * @throws IOException                  при любой IO ошибке.
     * @throws SAXException                 при ошибке SAX парсера.
     */
    public void doParse(@NonNull final InputBean bean)
        throws JAXBException, XMLStreamException, ParserConfigurationException, IOException, SAXException {
        if (log.isInfoEnabled())
            log.info("Начало работы программы");

        FileConverterFactory.create(bean)
            .convert();

        if (log.isInfoEnabled())
            log.info("Успешное завершение работы программы");
    }
}
