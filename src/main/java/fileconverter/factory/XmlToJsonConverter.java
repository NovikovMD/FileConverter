package fileconverter.factory;

import fileconverter.bean.InputBean;
import fileconverter.bean.json.JsonUpper;
import fileconverter.bean.xml.XmlUpper;
import fileconverter.converters.XmlToJson;
import fileconverter.readers.Reader;
import fileconverter.readers.xml.JaxbReader;
import fileconverter.writers.Writer;
import fileconverter.writers.json.GsonWriter;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * Конвертирует Xml файл в Json файл.
 */
public class XmlToJsonConverter extends AbstractConverter {
    //Настройки конвертации
    private final Reader<XmlUpper> reader;
    private final XmlToJson converter;
    private final Writer<JsonUpper> writer;

    public XmlToJsonConverter(final InputBean bean) throws JAXBException {
        super(bean);
        reader = new JaxbReader();
        converter = new XmlToJson();
        writer = new GsonWriter();
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
