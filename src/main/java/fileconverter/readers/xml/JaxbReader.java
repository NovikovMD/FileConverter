package fileconverter.readers.xml;

import fileconverter.bean.xml.XmlUpper;
import fileconverter.readers.Reader;
import lombok.extern.log4j.Log4j2;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.InputStream;

/**
 * Считывает Xml файл при помощи Jaxb.
 */
@Log4j2
public class JaxbReader implements Reader<XmlUpper> {
    private final JAXBContext context = JAXBContext.newInstance(XmlUpper.class);

    /**
     * @throws JAXBException при ошибке создания Jaxb парсера.
     */
    public JaxbReader() throws JAXBException {
        if (log.isDebugEnabled()) {
            log.debug("Создание Jaxb парсера.");
        }
    }

    /**
     *
     * @param stream источник Xml файла.
     * @return класс, содержащий данные из исходного Xml файла.
     * @throws JAXBException при ошибке Jaxb парсера.
     */
    @Override
    public XmlUpper parse(final InputStream stream) throws JAXBException {
        if (log.isDebugEnabled()) {
            log.debug("Начало работы Jaxb парсера");
        }

        return (XmlUpper) context.createUnmarshaller()
            .unmarshal(stream);
    }
}
