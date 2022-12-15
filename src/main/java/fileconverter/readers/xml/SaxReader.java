package fileconverter.readers.xml;

import fileconverter.bean.xml.XmlUpper;
import fileconverter.readers.Reader;
import lombok.extern.log4j.Log4j2;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Считывает Xml файл при помощи SAX.
 * Расширяет класс DefaultHandler в XMLHandler для работы с Xml файлом, используя SAX.
 */
@Log4j2
public class SaxReader implements Reader<XmlUpper> {
    //Инициализируется в XmlHandler
    private XmlUpper gameIndustry;
    private final SAXParserFactory factory = SAXParserFactory.newInstance();
    private final XmlHandler handler = new XmlHandler();

    /**
     * Считывает данные из Xml файла.
     *
     * @param file путь к существующему Xml файлу.
     * @return класс, содержащий данные из исходного Xml файла.
     * @throws ParserConfigurationException парсер не может быть создан
     *                                      в соответствии с заданной конфигурацией.
     * @throws SAXException                 в случае любой ошибки SAX парсера.
     * @throws IOException                  в случае любой IO ошибки.
     */
    @Override
    public XmlUpper parse(final String file)
        throws ParserConfigurationException, SAXException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("Начало работы Sax парсера");
        }

        try (final FileInputStream stream = new FileInputStream(file)) {
            factory.newSAXParser().parse(stream, handler);
        }

        if (log.isDebugEnabled()) {
            log.debug("Успешное завершение парсинга XML.");
        }
        return gameIndustry;
    }

    private class XmlHandler extends DefaultHandler {
        public void startDocument() {
            gameIndustry = new XmlUpper();
        }

        @Override
        public void startElement(final String uri, final String localName,
                                 final String qName, final Attributes attributes) {
            switch (qName) {
                case "Издатель" -> getGamePublisherSAX(attributes);
                case "Разработчик" -> getDeveloperStudioSAX(attributes);
                case "Игра" -> getGameSAX(attributes);
                case "Платформа" -> getPlatformSAX(attributes);
            }
        }

        private void getGamePublisherSAX(final Attributes attributes) {
            gameIndustry.addPublisher(attributes.getValue("наименование"));
        }

        private void getDeveloperStudioSAX(final Attributes attributes) {
            gameIndustry.getPublishers()
                .get(gameIndustry.returnLength() - 1)
                .addDevStudio(
                    attributes.getValue("наименование"),
                    Integer.parseInt(attributes.getValue("годОснования")),
                    attributes.getValue("URL"));
        }

        private void getGameSAX(final Attributes attributes) {
            gameIndustry.getPublishers()
                .get(gameIndustry.returnLength() - 1)
                .getDevStudios()
                .get(gameIndustry.getPublishers()
                    .get(gameIndustry.returnLength() - 1)
                    .returnLength() - 1)
                .addGame(
                    attributes.getValue("название"),
                    Integer.parseInt(attributes.getValue("годВыпуска")));
        }

        private void getPlatformSAX(final Attributes attributes) {
            gameIndustry.getPublishers()
                .get(gameIndustry.returnLength() - 1)
                .getDevStudios()
                .get(gameIndustry.getPublishers()
                    .get(gameIndustry.returnLength() - 1)
                    .returnLength() - 1)
                .getGames()
                .get(gameIndustry.getPublishers()
                    .get(gameIndustry.returnLength() - 1)
                    .getDevStudios()
                    .get(gameIndustry.getPublishers()
                        .get(gameIndustry.returnLength() - 1)
                        .returnLength() - 1)
                    .returnLength() - 1)
                .addPlatform(attributes.getValue("название"));
        }
    }
}
