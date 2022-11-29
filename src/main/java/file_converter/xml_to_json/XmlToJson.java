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
 * 1) parseXml - считывает XML файл, используя SAX.
 * 2) convert - конвертирует данные из Xml класса в Json класс.
 * 3) createJson - создает Json файл используя Jackson-databind.
 * Расширяет класс DefaultHandler в XMLHandler для работы с Xml файлом, используя SAX.
 */
public class XmlToJson {
    //Инициализируется в XmlHandler
    private XmlUpperClass gameIndustry;
    private final SAXParserFactory factory = SAXParserFactory.newInstance();
    private final XmlHandler handler = new XmlHandler();
    private final ObjectMapper mapper = new ObjectMapper();

    public void parseXml(final String path, final String newPath) throws Exception {
        Logger.getInstance().info("Начало работы парсинга XML");

        final XmlToJson parser = new XmlToJson();
        final XmlUpperClass parsedClass;
        try {
            parsedClass = parser.parseXml(path);
        } catch (ParserConfigurationException | SAXException | IOException exception) {
            Logger.getInstance().error("Не удалось считать файл XML.", exception);
            throw new Exception("Не удалось считать файл XML.", exception);
        } catch (IllegalArgumentException exception) {
            Logger.getInstance().error("Неверный путь к XML файлу.", exception);
            throw new Exception("Неверный путь к XML файлу.", exception);
        }
        Logger.getInstance().info("Успешное завершение парсинга XML.");

        try {
            parser.createJson(parser.convert(parsedClass), newPath);
        } catch (IOException ioException) {
            Logger.getInstance().error("Не удалось создать JSON файл.", ioException);
            throw new Exception("Не удалось создать JSON файл.", ioException);
        } catch (IllegalArgumentException exception) {
            Logger.getInstance().fatal("Не удалось конвертировать XML классы в JSON.", exception);
            throw new Exception("Не удалось конвертировать XML классы в JSON.", exception);
        }

        Logger.getInstance().info("Успешно создан JSON файл.");
    }

    /**
     * Считывает данные из Xml файла.
     *
     * @param path абсолютный путь к существующему Xml файлу.
     * @return класс, содержащий данные из исходного Xml файла.
     * @throws ParserConfigurationException парсер не может быть создан
     *                                      в соответствии с заданной конфигурацией.
     * @throws SAXException                 в случае любой ошибки SAX парсера.
     * @throws IOException                  в случае любой IO ошибки.
     * @throws IllegalArgumentException     в случае передачи параметром несуществующего файла.
     */
    public XmlUpperClass parseXml(final String path)
            throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException {
        Logger.getInstance().info("Начало работы парсинга XML");
        final SAXParser parser = factory.newSAXParser();

        final File file = new File(path);
        if (!file.exists())
            throw new IllegalArgumentException();

        parser.parse(file, handler);

        Logger.getInstance().info("Успешное завершение парсинга XML.");
        return gameIndustry;
    }

    /**
     * Конвертирует Xml классы данных в Json классы.
     *
     * @return класс, содержащий данные подобно Json файлу.
     * @throws IllegalArgumentException в случае передачи параметром null.
     */
    public JsonUpperClass convert(final XmlUpperClass gameIndustry) throws IllegalArgumentException {
        Logger.getInstance().info("Начало конвертирования классов");

        if (gameIndustry == null)
            throw new IllegalArgumentException();

        final JsonUpperClass jsonUpperClassGames = new JsonUpperClass();

        startConvert(gameIndustry, jsonUpperClassGames);

        Logger.getInstance().info("Конвертирование классов прошло успешно");
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
        final JsonGame checker = getCurrentGame(game.getName(), jsonUpperClassGames.getGames());

        if (checker == null) {
            createNewGame(jsonUpperClassGames, publisher, developer, game);
            return;
        }

        checker.addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());

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
                               final XmlDevStudio developer, final XmlGame game) {
        jsonUpperClassGames.addGame(game.getName(), game.getYear(), publisher.getName());

        getPlatform(game, jsonUpperClassGames.getGames().get(jsonUpperClassGames.returnLength() - 1));

        jsonUpperClassGames.getGames().get(jsonUpperClassGames.returnLength() - 1)
                .addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());
    }

    private void getPlatform(final XmlGame game, final JsonGame jsonGame) {
        for (int l = 0; l < game.returnLength(); l++) {
            jsonGame.addPlatform(game.getPlatforms().get(l).getName());
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
        Logger.getInstance().info("Начало создания файла JSON");

        mapper.writeValue(new File(path), jsonUpperClass);

        Logger.getInstance().info("Создание файла прошло успешно");
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
            gameIndustry.getPublishers().get(gameIndustry.returnLength() - 1)
                    .addDevStudio(attributes.getValue("name"),
                            Integer.parseInt(attributes.getValue("year_of_foundation")),
                            attributes.getValue("URL"));
        }

        private void getGameSAX(final Attributes attributes) {
            XmlGamePublisher publisher = gameIndustry.getPublishers().get(gameIndustry.returnLength() - 1);

            XmlDevStudio devStudio = publisher.getDevStudios().get(publisher.returnLength() - 1);

            devStudio.addGame(attributes.getValue("name"),
                    Integer.parseInt(attributes.getValue("year")));
        }

        private void getPlatformSAX(final Attributes attributes) {
            XmlGamePublisher publisher = gameIndustry.getPublishers().get(gameIndustry.returnLength() - 1);

            XmlDevStudio devStudio = publisher.getDevStudios().get(publisher.returnLength() - 1);

            XmlGame game = devStudio.getGames().get(devStudio.returnLength() - 1);
            game.addPlatform(attributes.getValue("name"));
        }
    }
}
