/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package fileconverter.xmltojson;

import fileconverter.bean.json.*;
import fileconverter.bean.xml.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.logging.log4j.Level;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Предоставляет доступ к трем методам:
 * 1) parseXml - считывает XML файл, используя SAX.
 * 2) convert - конвертирует данные из Xml класса в Json класс.
 * 3) createJson - создает Json файл используя Jackson-databind.
 * Расширяет класс DefaultHandler в XMLHandler для работы с Xml файлом, используя SAX.
 */
@Log4j2
public class XmlToJson {
    //Инициализируется в XmlHandler
    private XmlUpperClass gameIndustry;
    private final SAXParserFactory factory = SAXParserFactory.newInstance();
    private final XmlHandler handler = new XmlHandler();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Считывает данные из Xml файла.
     *
     * @param stream источник Xml файла.
     * @return класс, содержащий данные из исходного Xml файла.
     * @throws ParserConfigurationException парсер не может быть создан
     *                                      в соответствии с заданной конфигурацией.
     * @throws SAXException                 в случае любой ошибки SAX парсера.
     * @throws IOException                  в случае любой IO ошибки.
     * @throws IllegalArgumentException     в случае передачи параметром несуществующего файла.
     */
    public XmlUpperClass parseXml(final InputStream stream)
        throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException {
        log.log(Level.DEBUG, "Начало работы парсинга XML");

        factory.newSAXParser().parse(stream, handler);

        log.log(Level.DEBUG, "Успешное завершение парсинга XML.");
        return gameIndustry;
    }

    /**
     * Конвертирует Xml классы данных в Json классы.
     *
     * @return класс, содержащий данные подобно Json файлу.
     * @throws IllegalArgumentException в случае передачи параметром null.
     */
    public JsonUpperClass convert(@NonNull final XmlUpperClass gameIndustry) throws IllegalArgumentException {
        log.log(Level.DEBUG, "Начало конвертирования классов");

        final JsonUpperClass jsonUpperClassGames = new JsonUpperClass();

        startConvert(gameIndustry, jsonUpperClassGames);

        log.log(Level.DEBUG, "Конвертирование классов прошло успешно");
        return jsonUpperClassGames;
    }

    //region Convert private methods
    private void startConvert(final XmlUpperClass gameIndustry, final JsonUpperClass jsonUpperClassGames) {
        for (int index = 0; index < gameIndustry.returnLength(); index++) {
            getPublisher(jsonUpperClassGames, gameIndustry.getPublishers().get(index));
        }
    }

    private void getPublisher(final JsonUpperClass jsonUpperClassGames, final XmlGamePublisher publisher) {
        for (int index = 0; index < publisher.returnLength(); index++) {
            getDeveloper(jsonUpperClassGames, publisher, publisher.getDevStudios().get(index));
        }
    }

    private void getDeveloper(final JsonUpperClass jsonUpperClassGames, final XmlGamePublisher publisher,
                              final XmlDevStudio developer) {
        for (int index = 0; index < developer.returnLength(); index++) {
            getGame(jsonUpperClassGames, publisher, developer, developer.getGames().get(index));
        }
    }

    private void getGame(final JsonUpperClass jsonUpperClassGames, final XmlGamePublisher publisher,
                         final XmlDevStudio developer, final XmlGame game) {

        if (getCurrentGame(game.getName(), jsonUpperClassGames.getGames()) == null) {
            createNewGame(jsonUpperClassGames, publisher, developer, game);
            return;
        }

        try {
            Objects.requireNonNull(getCurrentGame(game.getName(), jsonUpperClassGames.getGames()))
                .addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());
        }
        catch (NullPointerException exception){
            log.log(Level.WARN,"Получен null при конвертации классов.");
        }

    }

    private JsonGame getCurrentGame(final String nameToFind, final ArrayList<JsonGame> listToLookIn) {
        for (val jsonGame : listToLookIn) {
            if (jsonGame.getName().equals(nameToFind)) {
                return jsonGame;
            }
        }
        return null;
    }

    private void createNewGame(final JsonUpperClass jsonUpperClassGames, final XmlGamePublisher publisher,
                               final XmlDevStudio developer, final XmlGame game) {
        jsonUpperClassGames.addGame(game.getName(), game.getYear(), publisher.getName());

        getPlatform(game, jsonUpperClassGames.getGames().get(jsonUpperClassGames.returnLength() - 1));

        jsonUpperClassGames.getGames().get(jsonUpperClassGames.returnLength() - 1)
            .addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());
    }

    private void getPlatform(final XmlGame game, final JsonGame jsonGame) {
        for (int index = 0; index < game.returnLength(); index++) {
            jsonGame.addPlatform(game.getPlatforms().get(index).getName());
        }
    }
    //endregion

    /**
     * Создает Json файл
     *
     * @param jsonUpperClass класс, содержащий данные для Json файла
     *                       (заполняется в методе convert).
     * @param stream         приёмник данных для Json файла.
     * @throws IOException если произошла ошибка зависи в файл.
     */
    public void createJson(final JsonUpperClass jsonUpperClass, final OutputStream stream) throws IOException {
        log.log(Level.DEBUG, "Начало создания файла JSON");

        mapper.writeValue(stream, jsonUpperClass);

        log.log(Level.DEBUG, "Создание файла прошло успешно");
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
                .addDevStudio(attributes.getValue("name"),
                    Integer.parseInt(attributes.getValue("year_of_foundation")),
                    attributes.getValue("URL"));
        }

        private void getGameSAX(final Attributes attributes) {
            gameIndustry.getPublishers()
                .get(gameIndustry.returnLength() - 1)
                .getDevStudios()
                .get(gameIndustry.getPublishers()
                    .get(gameIndustry.returnLength() - 1).returnLength() - 1)
                .addGame(attributes.getValue("name"),
                    Integer.parseInt(attributes.getValue("year")));
        }

        private void getPlatformSAX(final Attributes attributes) {
            gameIndustry.getPublishers()
                .get(gameIndustry.returnLength() - 1)
                .getDevStudios()
                .get(gameIndustry.getPublishers()
                    .get(gameIndustry.returnLength() - 1).returnLength() - 1)
                .getGames()
                .get(gameIndustry.getPublishers()
                    .get(gameIndustry.returnLength() - 1)
                    .getDevStudios()
                    .get(gameIndustry.getPublishers()
                        .get(gameIndustry.returnLength() - 1).returnLength() - 1)
                    .returnLength() - 1)
                .addPlatform(attributes.getValue("name"));
        }
    }
}
