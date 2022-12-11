package fileconverter.readers.xml;

import fileconverter.bean.xml.XmlUpperClass;
import fileconverter.readers.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.InputStream;

public class JaxbReader implements Reader<XmlUpperClass> {
    @Override
    public XmlUpperClass parse(InputStream stream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(XmlUpperClass.class);
        return (XmlUpperClass) context.createUnmarshaller()
            .unmarshal(stream);
    }
}
