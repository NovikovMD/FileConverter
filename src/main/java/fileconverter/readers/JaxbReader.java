package fileconverter.readers;

import fileconverter.bean.xml.XmlUpperClass;

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
