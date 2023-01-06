package ru.itdt.fileconverter.readers.xml;

import lombok.extern.log4j.Log4j2;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.itdt.fileconverter.bean.xml.*;
import ru.itdt.fileconverter.readers.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Считывает Xml файл при помощи SAX.
 * Расширяет класс DefaultHandler в XMLHandler для работы с Xml файлом, используя SAX.
 */
@Log4j2
public class SaxReader implements Reader<XmlRoot> {
    //Инициализируется в XmlHandler
    private XmlRoot gameIndustry;
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
    public XmlRoot parse(final String file)
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
            gameIndustry = new XmlRoot();
        }

        @Override
        public void startElement(final String uri, final String localName,
                                 final String qName, final Attributes attributes) {
            switch (qName) {
                case "Издатель" -> addGamePublisherSAX(attributes);
                case "Разработчик" -> addDeveloperStudioSAX(attributes);
                case "Игра" -> addGameSAX(attributes);
                case "Платформа" -> addPlatformSAX(attributes);
            }
        }

        private void addGamePublisherSAX(final Attributes attributes) {
            gameIndustry.getPublishers()
                .add(
                    new XmlGamePublisher(attributes.getValue("наименование")));
        }

        private void addDeveloperStudioSAX(final Attributes attributes) {
            gameIndustry.getPublishers()
                .get(gameIndustry.getPublishers()
                    .size() - 1)
                .getDevStudios()
                .add(XmlDevStudio.builder()
                    .name(attributes.getValue("наименование"))
                    .yearOfFoundation(
                        Integer.parseInt(attributes.getValue("годОснования")))
                    .url(attributes.getValue("URL"))
                    .build());
        }

        private void addGameSAX(final Attributes attributes) {
            gameIndustry.getPublishers()
                .get(gameIndustry.getPublishers()
                    .size() - 1)
                .getDevStudios()
                .get(gameIndustry.getPublishers()
                    .get(gameIndustry.getPublishers()
                        .size() - 1)
                    .getDevStudios()
                    .size() - 1)
                .getGames()
                .add(XmlGame.builder()
                    .name(attributes.getValue("название"))
                    .year(
                        Integer.parseInt(attributes.getValue("годВыпуска")))
                    .build());
        }

        private void addPlatformSAX(final Attributes attributes) {
            gameIndustry.getPublishers()
                .get(gameIndustry.getPublishers()
                    .size() - 1)
                .getDevStudios()
                .get(gameIndustry.getPublishers()
                    .get(gameIndustry.getPublishers()
                        .size() - 1)
                    .getDevStudios()
                    .size() - 1)
                .getGames()
                .get(gameIndustry.getPublishers()
                    .get(gameIndustry.getPublishers()
                        .size() - 1)
                    .getDevStudios()
                    .get(gameIndustry.getPublishers()
                        .get(gameIndustry.getPublishers()
                            .size() - 1)
                        .getDevStudios()
                        .size() - 1)
                    .getGames()
                    .size()- 1)
                .getPlatforms()
                .add(
                    new XmlPlatform(attributes.getValue("название")));
        }
    }
}
