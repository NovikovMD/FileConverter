package ru.itdt.fileconverter.readers;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface Reader<T> {
    /**
     * Считывает входной файл.
     *
     * @param file путь к существующему файлу.
     * @return класс данных с данными из считанного файла.
     * @throws ParserConfigurationException при ошибке создания SAX парсера.
     * @throws SAXException                 при ошибке работы SAX парсера.
     * @throws IOException                  при любой IO ошибке.
     * @throws JAXBException                при ошибке Jaxb парсера.
     */
    T parse(final String file)
        throws ParserConfigurationException, SAXException, IOException, JAXBException;
}
