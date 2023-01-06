package ru.itdt.fileconverter.factory;

import ru.itdt.fileconverter.bean.InputParams;
import ru.itdt.fileconverter.bean.json.JsonRoot;
import ru.itdt.fileconverter.bean.xml.XmlRoot;
import ru.itdt.fileconverter.converters.JsonToXml;
import ru.itdt.fileconverter.readers.Reader;
import ru.itdt.fileconverter.readers.json.JacksonReader;
import ru.itdt.fileconverter.writers.Writer;
import ru.itdt.fileconverter.writers.xml.JaxbWriter;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * Конвертирует Json файл в Xml файл.
 */
public class JsonToXmlConverter extends AbstractConverter {
    //Настройки конвертации
    private final Reader<JsonRoot> reader;
    private final JsonToXml converter;
    private final Writer<XmlRoot> writer;

    public JsonToXmlConverter(final InputParams bean) throws JAXBException {
        super(bean);
        reader = new JacksonReader();
        converter = new JsonToXml();
        writer = new JaxbWriter();
    }

    @Override
    public void convert()
        throws JAXBException, ParserConfigurationException, IOException, SAXException, XMLStreamException {
        writer.write(
            converter.convert(
                reader.parse(bean.getExistingFile())),
            bean.getNewFile());
    }
}
