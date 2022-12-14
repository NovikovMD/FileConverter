package fileconverter.readers;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public interface Reader<T> {
    /**
     * Считывает входной файл.
     *
     * @param stream //нужно переделать
     * @return класс данных с данными из считанного файла.
     * @throws ParserConfigurationException при ошибке создания SAX парсера.
     * @throws SAXException                 при ошибке работы SAX парсера.
     * @throws IOException                  при любой IO ошибке.
     * @throws JAXBException                при ошибке Jaxb парсера.
     */
    T parse(final InputStream stream)
        throws ParserConfigurationException, SAXException, IOException, JAXBException;
}
