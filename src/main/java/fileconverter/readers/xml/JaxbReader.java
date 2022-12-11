package fileconverter.readers.xml;

import fileconverter.bean.xml.XmlUpperClass;
import fileconverter.readers.Reader;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.InputStream;

/**
 * Считывает Xml файл при помощи Jaxb.
 */
@Log4j2
public class JaxbReader implements Reader<XmlUpperClass> {
    private final JAXBContext context = JAXBContext.newInstance(XmlUpperClass.class);

    /**
     * @throws JAXBException при ошибке создания Jaxb парсера.
     */
    public JaxbReader() throws JAXBException {
        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Создание Jaxb парсера.");
    }

    /**
     *
     * @param stream источник Xml файла.
     * @return класс, содержащий данные из исходного Xml файла.
     * @throws JAXBException при ошибке Jaxb парсера.
     */
    @Override
    public XmlUpperClass parse(final InputStream stream) throws JAXBException {
        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Начало работы Jaxb парсера");

        return (XmlUpperClass) context.createUnmarshaller()
            .unmarshal(stream);
    }
}
