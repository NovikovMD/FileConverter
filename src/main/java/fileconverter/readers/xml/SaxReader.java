package fileconverter.readers.xml;

import fileconverter.bean.xml.XmlUpperClass;
import fileconverter.readers.Reader;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Считывает Xml файл при помощи SAX.
 * Расширяет класс DefaultHandler в XMLHandler для работы с Xml файлом, используя SAX.
 */
@Log4j2
public class SaxReader implements Reader<XmlUpperClass> {
    //Инициализируется в XmlHandler
    private XmlUpperClass gameIndustry;
    private final SAXParserFactory factory = SAXParserFactory.newInstance();
    private final XmlHandler handler = new XmlHandler();

    /**
     * Считывает данные из Xml файла.
     *
     * @param stream источник Xml файла.
     * @return класс, содержащий данные из исходного Xml файла.
     * @throws ParserConfigurationException парсер не может быть создан
     *                                      в соответствии с заданной конфигурацией.
     * @throws SAXException                 в случае любой ошибки SAX парсера.
     * @throws IOException                  в случае любой IO ошибки.
     */
    @Override
    public XmlUpperClass parse(final InputStream stream)
        throws ParserConfigurationException, SAXException, IOException {
        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Начало работы Sax парсера");

        factory.newSAXParser().parse(stream, handler);

        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Успешное завершение парсинга XML.");
        return gameIndustry;
    }

    private class XmlHandler extends DefaultHandler {
        public void startDocument() {
            gameIndustry = new XmlUpperClass();
        }

        @Override
        public void startElement(final String uri, final String localName,
                                 final String qName, final Attributes attributes) {
            switch (qName) {
                case "gamePublisher" -> getGamePublisherSAX(attributes);
                case "developerStudio" -> getDeveloperStudioSAX(attributes);
                case "game" -> getGameSAX(attributes);
                case "platform" -> getPlatformSAX(attributes);
            }
        }

        private void getGamePublisherSAX(final Attributes attributes) {
            gameIndustry.addPublisher(attributes.getValue("name"));
        }

        private void getDeveloperStudioSAX(final Attributes attributes) {
            gameIndustry.getPublishers()
                .get(gameIndustry.returnLength() - 1)
                .addDevStudio(
                    attributes.getValue("name"),
                    Integer.parseInt(attributes.getValue("year_of_foundation")),
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
                    attributes.getValue("name"),
                    Integer.parseInt(attributes.getValue("year")));
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
                .addPlatform(attributes.getValue("name"));
        }
    }
}
