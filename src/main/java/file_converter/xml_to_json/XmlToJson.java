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
    public XmlUpperClass parseXml(final String path) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException {
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
        if (gameIndustry == null)
            throw new IllegalArgumentException();

        JsonUpperClass jsonUpperClassGames = new JsonUpperClass();

        startConvert(gameIndustry, jsonUpperClassGames);

        return jsonUpperClassGames;
    }

    //region Convert private methods
    private void startConvert(final XmlUpperClass gameIndustry, final JsonUpperClass jsonUpperClassGames) {
        for (int i = 0; i < gameIndustry.returnLength(); i++) {
            //get current publisher
            XmlGamePublisher publisher = gameIndustry.getPublishers().get(i);

            getPublisher(jsonUpperClassGames, publisher);
        }
    }

    private void getPublisher(final JsonUpperClass jsonUpperClassGames, final XmlGamePublisher publisher) {
        for (int j = 0; j < publisher.returnLength(); j++) {
            //get current developer
            XmlDevStudio developer = publisher.getDevStudios().get(j);

            getDeveloper(jsonUpperClassGames, publisher, developer);
        }
    }

    private void getDeveloper(final JsonUpperClass jsonUpperClassGames, final XmlGamePublisher publisher,
                              final XmlDevStudio developer) {
        for (int k = 0; k < developer.returnLength(); k++) {
            //get current game
            XmlGame game = developer.getGames().get(k);

            getGame(jsonUpperClassGames, publisher, developer, game);
        }
    }

    private void getGame(final JsonUpperClass jsonUpperClassGames, final XmlGamePublisher publisher,
                         final XmlDevStudio developer, final XmlGame game) {
        JsonGame checker = getCurrentGame(game.getName(), jsonUpperClassGames.getGames());
        //add current game to list, if it doesn't exist so far
        if (checker == null) {
            createNewGame(jsonUpperClassGames, publisher, developer, game);
        } else {
            //if we have current game in list, we need to add developer studio to it
            checker.addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());
        }
    }

    private JsonGame getCurrentGame(final String nameToFind, final ArrayList<JsonGame> listToLookIn) {
        JsonGame foundGame = null;

        for (JsonGame jsoNgame : listToLookIn) {
            if (jsoNgame.getName().equals(nameToFind)) {
                foundGame = jsoNgame;
            }
        }

        return foundGame;
    }

    private void createNewGame(final JsonUpperClass jsonUpperClassGames, final XmlGamePublisher publisher,
                               XmlDevStudio developer, XmlGame game) {
        jsonUpperClassGames.addGame(game.getName(), game.getYear(), publisher.getName());
        JsonGame jsonGame = jsonUpperClassGames.getGames().get(jsonUpperClassGames.returnLength() - 1);

        getPlatform(game, jsonGame);

        jsonGame.addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());
    }

    private void getPlatform(final XmlGame game, final JsonGame jsonGame) {
        for (int l = 0; l < game.returnLength(); l++) {
            XmlPlatform platform = game.getPlatforms().get(l);

            jsonGame.addPlatform(platform.getName());
        }
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
            String name = attributes.getValue("name");
            gameIndustry.addPublisher(name);
        }

        private void getDeveloperStudioSAX(final Attributes attributes) {
            String name = attributes.getValue("name");
            int year = Integer.parseInt(attributes.getValue("year_of_foundation"));
            String URL = attributes.getValue("URL");
            gameIndustry.getPublishers().get(gameIndustry.returnLength() - 1).addDevStudio(name, year, URL);
        }

        private void getGameSAX(final Attributes attributes) {
            String name = attributes.getValue("name");
            int year = Integer.parseInt(attributes.getValue("year"));

            int gameIndustryListLength = gameIndustry.returnLength() - 1;

            XmlGamePublisher publisher = gameIndustry.getPublishers().get(gameIndustryListLength);
            int publisherListLength = publisher.returnLength() - 1;

            XmlDevStudio devStudio = publisher.getDevStudios().get(publisherListLength);

            devStudio.addGame(name, year);
        }

        private void getPlatformSAX(final Attributes attributes) {
            String name = attributes.getValue("name");

            int gameIndustryListLength = gameIndustry.returnLength() - 1;

            XmlGamePublisher publisher = gameIndustry.getPublishers().get(gameIndustryListLength);
            int publisherListLength = publisher.returnLength() - 1;

            XmlDevStudio devStudio = publisher.getDevStudios().get(publisherListLength);
            int devStudioListLength = devStudio.returnLength() - 1;

            XmlGame game = devStudio.getGames().get(devStudioListLength);
            game.addPlatform(name);
        }
    }

}
