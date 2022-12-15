package fileconverter.writers;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public interface Writer<T> {
    /**
     * Создает файл, основываясь на входных данных
     *
     * @param data   класс данных, на основе которых требуется создать файл.
     * @param newFile путь к новому файлу
     * @throws IOException        при любой IO ошибке.
     * @throws XMLStreamException при ошибке заполнения файла.
     * @throws JAXBException      при ошибке Jaxb парсера.
     */
    void write(final T data, final String newFile) throws IOException, XMLStreamException, JAXBException;
}
