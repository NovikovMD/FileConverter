package ru.itdt.fileconverter.readers.xml;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import ru.itdt.fileconverter.bean.xml.XmlRoot;
import ru.itdt.fileconverter.readers.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Считывает Xml файл при помощи Jaxb.
 */
@Log4j2
public class JaxbReader implements Reader<XmlRoot> {
    private static final JAXBContext context;
    static {
        try {
            context = JAXBContext.newInstance(XmlRoot.class);
        } catch (JAXBException e) {
            throw new AssertionError();
        }
    }

    private final Unmarshaller unmarshaller = context.createUnmarshaller();

    /**
     * @throws JAXBException при ошибке создания Jaxb парсера.
     */
    public JaxbReader() throws JAXBException {
        if (log.isDebugEnabled()) {
            log.debug("Создание Jaxb парсера.");
        }
    }

    /**
     * @param file источник Xml файла.
     * @return класс, содержащий данные из исходного Xml файла.
     * @throws JAXBException при ошибке Jaxb парсера.
     * @throws IOException   при любой IO ошибке.
     */
    @Override
    public XmlRoot parse(final String file) throws JAXBException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("Начало работы Jaxb парсера");
        }

        try (val stream = new FileInputStream(file)) {
            return unmarshaller.unmarshal(new StreamSource(stream), XmlRoot.class).getValue();
        }
    }
}
