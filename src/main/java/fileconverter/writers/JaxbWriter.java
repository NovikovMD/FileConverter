package fileconverter.writers;

import fileconverter.bean.xml.XmlUpperClass;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;

public class JaxbWriter implements Writer<XmlUpperClass> {

    private final JAXBContext context = JAXBContext.newInstance(XmlUpperClass.class);
    private final Marshaller mar = context.createMarshaller();

    public JaxbWriter() throws JAXBException {
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    }

    @Override
    public void write(XmlUpperClass upperClass, OutputStream stream) throws JAXBException {
        mar.marshal(upperClass, stream);
    }
}
