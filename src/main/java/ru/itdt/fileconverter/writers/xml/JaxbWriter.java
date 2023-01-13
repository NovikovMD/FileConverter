package ru.itdt.fileconverter.writers.xml;

import ru.itdt.fileconverter.bean.xml.XmlRoot;
import ru.itdt.fileconverter.writers.Writer;
import lombok.extern.log4j.Log4j2;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Создает XML файл, используя Jaxb.
 */
@Log4j2
public class JaxbWriter implements Writer<XmlRoot> {
    private static final JAXBContext context;
    static {
        try {
            context = JAXBContext.newInstance(XmlRoot.class);
        } catch (JAXBException exception) {
            throw new RuntimeException(exception);
        }
    }

    private final Marshaller mar = context.createMarshaller();

    /**
     * @throws JAXBException если проихошла ошибка создания Jaxb парсера.
     */
    public JaxbWriter() throws JAXBException {
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    }

    /**
     * Запускает создание Xml файла.
     *
     * @param data    класс, содержащий данные для Xml файла.
     * @param newFile путь к новому фойлу.
     * @throws JAXBException если проихошла ошибка Jaxb парсера.
     * @throws IOException   если проихошла IO ошибка.
     */
    @Override
    public void write(final XmlRoot data, final String newFile) throws JAXBException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("Начало создания файла Jaxb");
        }

        try (final FileOutputStream stream = new FileOutputStream(newFile)) {
            mar.marshal(data, stream);
        }

        if (log.isDebugEnabled()) {
            log.debug("Создание файла Xml прошло успешно");
        }
    }
}
