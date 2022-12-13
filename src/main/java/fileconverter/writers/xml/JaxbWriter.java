package fileconverter.writers.xml;

import fileconverter.bean.xml.XmlUpper;
import fileconverter.writers.Writer;
import lombok.extern.log4j.Log4j2;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;

/**
 * Создает XML файл, используя Jaxb.
 */
@Log4j2
public class JaxbWriter implements Writer<XmlUpper> {
    private final JAXBContext context = JAXBContext.newInstance(XmlUpper.class);
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
     * @param upperClass класс, содержащий данные для Xml файла.
     * @param stream     приёмник данных для Xml файла.
     * @throws JAXBException если проихошла ошибка Jaxb парсера.
     */
    @Override
    public void write(final XmlUpper upperClass, final OutputStream stream) throws JAXBException {
        if (log.isDebugEnabled()) {
            log.debug("Начало создания файла Jaxb");
        }

        mar.marshal(upperClass, stream);

        if (log.isDebugEnabled()) {
            log.debug("Создание файла Xml прошло успешно");
        }
    }
}
