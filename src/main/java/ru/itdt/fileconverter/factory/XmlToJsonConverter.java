package ru.itdt.fileconverter.factory;

import ru.itdt.fileconverter.bean.InputParams;
import ru.itdt.fileconverter.bean.json.JsonRoot;
import ru.itdt.fileconverter.bean.xml.XmlRoot;
import ru.itdt.fileconverter.converters.XmlToJson;
import ru.itdt.fileconverter.readers.Reader;
import ru.itdt.fileconverter.readers.xml.JaxbReader;
import ru.itdt.fileconverter.writers.Writer;
import ru.itdt.fileconverter.writers.json.GsonWriter;
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
    private final Reader<XmlRoot> reader;
    private final XmlToJson converter;
    private final Writer<JsonRoot> writer;

    public XmlToJsonConverter(final InputParams bean) throws JAXBException {
        super(bean);
        reader = new JaxbReader();
        converter = new XmlToJson();
        writer = new GsonWriter();
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
