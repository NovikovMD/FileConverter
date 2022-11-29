/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package file_converter.xml_to_json;

import file_converter.classes.json.*;
import file_converter.classes.xml.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import logger.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Предоставляет доступ к трем методам:
 * 1) parseXml - считывает XML файл, используя SAX. Сохраняет данные в gameIndustry
 * 2) convert - конвертирует данные из Xml класса в Json класс.
 * 3) createJson - создает Json файл используя Jackson-databind.
 * Расширяет класс DefaultHandler в XMLHandler для работы с Xml файлом, используя SAX
 */
public class XmlToJson {
    //Инициализируется в XmlHandler
    private XmlUpperClass gameIndustry;
    private final SAXParserFactory factory = SAXParserFactory.newInstance();
    private final XmlHandler handler = new XmlHandler();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Считывает данные из Xml файла.
     *
     * @param path абсолютный путь к существующему Xml файлу.
     * @return класс, содержащий даныне из исходного Xml файла.
     * @throws ParserConfigurationException парсер не может быть создан
     *                                      в соответствии с заданной окнфигурацией.
     * @throws SAXException                 в случае любой ошибки SAX парсера.
     * @throws IOException                  в случае любой IO ошибки.
     * @throws IllegalArgumentException     в случае передачи параметром несуществующего файла.
     */
    public XmlUpperClass parseXml(final String path)
            throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException {
        SAXParser parser = factory.newSAXParser();

        File fl = new File(path);
        if (!fl.exists())
            throw new IllegalArgumentException();

        parser.parse(fl, handler);

        return gameIndustry;
    }

    /**
     * Конвертирует Xml классы данных в Json классы.
     *
     * @return класс, содержащий данные подобно Json файлу.
     * @throws IllegalArgumentException в случае передачи параметром null.
     */
    public JsonUpperClass convert(final XmlUpperClass gameIndustry) throws IllegalArgumentException {
        Logger.getInstance().debug("Начало конвертирования классов");
        if (gameIndustry == null)
            throw new IllegalArgumentException();

        JsonUpperClass jsonUpperClassGames = new JsonUpperClass();

        startConvert(gameIndustry, jsonUpperClassGames);

        Logger.getInstance().info("Успешно завершено конвертирование классов");
        return jsonUpperClassGames;
    }

    //region Convert private methods
    private void startConvert(final XmlUpperClass gameIndustry, final JsonUpperClass jsonUpperClassGames) {
        for (int i = 0; i < gameIndustry.returnLength(); i++) {
            Logger.getInstance().debug("Начало обработки gamePublisher" + i);

            getPublisher(jsonUpperClassGames, gameIndustry.getPublishers().get(i));

            Logger.getInstance().debug("Конец обработки gamePublisher" + i);
        }
    }

    private void getPublisher(final JsonUpperClass jsonUpperClassGames, final XmlGamePublisher publisher) {
        for (int j = 0; j < publisher.returnLength(); j++) {
            Logger.getInstance().debug("Начало обработки devStudio" + j);

            getDeveloper(jsonUpperClassGames, publisher, publisher.getDevStudios().get(j));

            Logger.getInstance().debug("Конец обработки devStudio" + j);
        }
    }

    private void getDeveloper(final JsonUpperClass jsonUpperClassGames, final XmlGamePublisher publisher,
                              final XmlDevStudio developer) {
        for (int k = 0; k < developer.returnLength(); k++) {

            Logger.getInstance().debug("Начало обработки game" + k);

            getGame(jsonUpperClassGames, publisher, developer, developer.getGames().get(k));

            Logger.getInstance().debug("Конец обработки game" + k);
        }
    }

    private void getGame(final JsonUpperClass jsonUpperClassGames, final XmlGamePublisher publisher,
                         final XmlDevStudio developer, final XmlGame game) {
        JsonGame checker = getCurrentGame(game.getName(), jsonUpperClassGames.getGames());
        //add current game to list, if it doesn't exist so far
        if (checker == null) {
            Logger.getInstance().debug("Создание ранее не существующей game");
            createNewGame(jsonUpperClassGames, publisher, developer, game);
        } else {
            Logger.getInstance().debug("Добавление к game нового developer");
            //if we have current game in list, we need to add developer studio to it
            checker.addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());
        }
    }

    private JsonGame getCurrentGame(final String nameToFind, final ArrayList<JsonGame> listToLookIn) {
        for (JsonGame jsoNgame : listToLookIn) {
            if (jsoNgame.getName().equals(nameToFind)) {
                return jsoNgame;
            }
        }

        return null;
    }

    private void createNewGame(final JsonUpperClass jsonUpperClassGames, final XmlGamePublisher publisher,
                               XmlDevStudio developer, XmlGame game) {
        jsonUpperClassGames.addGame(game.getName(), game.getYear(), publisher.getName());
        JsonGame jsonGame = jsonUpperClassGames.getGames().get(jsonUpperClassGames.returnLength() - 1);

        getPlatform(game, jsonGame);

        jsonGame.addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());
    }

    private void getPlatform(final XmlGame game, final JsonGame jsonGame) {
        Logger.getInstance().debug("Начало считывания platform");
        for (int l = 0; l < game.returnLength(); l++) {
            jsonGame.addPlatform(game.getPlatforms().get(l).getName());
        }
        Logger.getInstance().debug("Конец считывания platform");
    }
    //endregion

    /**
     * Создает Json файл
     *
     * @param jsonUpperClass класс, содержащий данные для Json файла
     *                       (заполняется в методе convert).
     * @param path           абсолютный путь к новому Json файлу.
     */
    public void createJson(final JsonUpperClass jsonUpperClass, final String path) throws IOException {
        Logger.getInstance().debug("Начало создания файла JSON");
        mapper.writeValue(new File(path), jsonUpperClass);
    }

    private class XmlHandler extends DefaultHandler {
        public void startDocument() {
            gameIndustry = new XmlUpperClass();
        }

        @Override
        public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) {
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
            String name = attributes.getValue("name");
            int year = Integer.parseInt(attributes.getValue("year_of_foundation"));
            String URL = attributes.getValue("URL");
            gameIndustry.getPublishers().get(gameIndustry.returnLength() - 1).addDevStudio(name, year, URL);
        }

        private void getGameSAX(final Attributes attributes) {

            XmlGamePublisher publisher = gameIndustry.getPublishers().get(gameIndustry.returnLength() - 1);

            XmlDevStudio devStudio = publisher.getDevStudios().get(publisher.returnLength() - 1);

            devStudio.addGame(attributes.getValue("name"), Integer.parseInt(attributes.getValue("year")));
        }

        private void getPlatformSAX(final Attributes attributes) {

            XmlGamePublisher publisher = gameIndustry.getPublishers().get(gameIndustry.returnLength() - 1);

            XmlDevStudio devStudio = publisher.getDevStudios().get(publisher.returnLength() - 1);

            XmlGame game = devStudio.getGames().get(devStudio.returnLength() - 1);
            game.addPlatform(attributes.getValue("name"));
        }
    }

}
