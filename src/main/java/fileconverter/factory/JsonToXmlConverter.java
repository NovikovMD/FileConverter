package fileconverter.factory;

import fileconverter.bean.InputBean;
import fileconverter.bean.json.JsonUpper;
import fileconverter.bean.xml.XmlUpper;
import fileconverter.converters.JsonToXml;
import fileconverter.readers.Reader;
import fileconverter.readers.json.JacksonReader;
import fileconverter.writers.Writer;
import fileconverter.writers.xml.JaxbWriter;
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
    private final Reader<JsonUpper> reader;
    private final JsonToXml converter;
    private final Writer<XmlUpper> writer;

    public JsonToXmlConverter(final InputBean bean) throws JAXBException {
        super(bean);
        reader = new JacksonReader();
        converter = new JsonToXml();
        writer = new JaxbWriter();
    }

    @Override
    public void convert()
        throws JAXBException, ParserConfigurationException, IOException, SAXException, XMLStreamException {
        try {
            writer.write(
                converter.convert(
                    reader.parse(bean.getExistingFile())),
                bean.getNewFile());
        } finally {
            bean.getExistingFile().close();
            bean.getNewFile().close();
        }
    }
}
