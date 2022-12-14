package fileconverter.factory;

import fileconverter.bean.InputBean;
import lombok.RequiredArgsConstructor;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

@RequiredArgsConstructor
public abstract class AbstractConverter {
    protected final InputBean bean;

    /**
     * Считывает входной (xml\json) файл и создает (json\xml) файл соответственно.
     *
     * @throws JAXBException                при ошибке Jaxb парсера.
     * @throws ParserConfigurationException при ошибке создания SAX парсера.
     * @throws IOException                  при любой IO ошибке.
     * @throws SAXException                 при ошибке работы SAX парсера.
     * @throws XMLStreamException           при ошибке заполнения файла.
     */
    public abstract void convert()
        throws JAXBException, ParserConfigurationException, IOException, SAXException, XMLStreamException;
}
