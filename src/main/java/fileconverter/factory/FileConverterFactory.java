package fileconverter.factory;

import fileconverter.bean.InputBean;

import javax.xml.bind.JAXBException;

/**
 * Фабрика для создания ковертера.
 */
public class FileConverterFactory {
    /**
     * Создает объекты для конвертирования
     * @param bean заполненный InputBean.
     * @return объект конвертера для входных данных.
     * @throws JAXBException при ошибке создания Jaxb парсера.
     */
    public static AbstractConverter create(final InputBean bean) throws JAXBException {
        return switch (bean.getExistingFileExtension()) {
            case "xml" -> new XmlToJsonConverter(bean);
            case "json" -> new JsonToXmlConverter(bean);
            default -> throw new IllegalArgumentException("Некорректное расширение файла");
        };
    }
}
