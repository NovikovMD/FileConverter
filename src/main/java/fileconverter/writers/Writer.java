package fileconverter.writers;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStream;

public interface Writer<T> {
    /**
     * Создает файл, основываясь на входных данных
     *
     * @param data   класс данных, на основе которых требуется создать файл.
     * @param stream //нужно переделать
     * @throws IOException        при любой IO ошибке.
     * @throws XMLStreamException при ошибке заполнения файла.
     * @throws JAXBException      при ошибке Jaxb парсера.
     */
    void write(final T data, final OutputStream stream) throws IOException, XMLStreamException, JAXBException;
}
