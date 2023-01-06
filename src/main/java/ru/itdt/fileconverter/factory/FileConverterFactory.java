package ru.itdt.fileconverter.factory;

import ru.itdt.fileconverter.bean.InputParams;

import javax.xml.bind.JAXBException;

import static org.apache.commons.io.FilenameUtils.getExtension;

/**
 * Фабрика для создания ковертера.
 */
public class FileConverterFactory {
    /**
     * Создает объекты для конвертирования
     *
     * @param bean заполненный InputBean.
     * @return объект конвертера для входных данных.
     * @throws JAXBException при ошибке создания Jaxb парсера.
     */
    public static AbstractConverter create(final InputParams bean) throws JAXBException {
        return switch (getExtension(bean.getExistingFile())) {
            case "xml" -> new XmlToJsonConverter(bean);
            case "json" -> new JsonToXmlConverter(bean);
            default -> throw new IllegalArgumentException("Некорректное расширение файла");
        };
    }
}
