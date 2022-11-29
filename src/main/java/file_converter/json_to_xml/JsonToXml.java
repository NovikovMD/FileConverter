/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package file_converter.json_to_xml;

import file_converter.classes.json.JsonUpperClass;
import file_converter.classes.json.JsonDevStudio;
import file_converter.classes.json.JsonGame;
import file_converter.classes.xml.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Предоставляет доступ к трем методам:
 * 1) parseJson - считывает данные из Json файла, используя Jackson.
 * 2) convert - конвертирует Json классы данных в XML классы.
 * 3) createXML - создает XML файл, используя StAX.
 */
public class JsonToXml {
    private final JsonFactory factory = new JsonFactory();
    private final XMLOutputFactory output = XMLOutputFactory.newInstance();

    /**
     * Считывает данные из Json файла.
     *
     * @param path абсолютный путь к существующему Json файлу.
     * @return класс, содержащий данные из исходного Json файла.
     * @throws IOException              если считывание Json файла было прервано.
     * @throws IllegalArgumentException если передан неверный путь к Json файлу
     *                                  или некорректная структура файла.
     */
    public JsonUpperClass parseJson(final String path) throws IOException, IllegalArgumentException {
        //Logger.getInstance().info("Начало работы парсинга Json");
        final JsonUpperClass games = new JsonUpperClass();


        final File fl = new File(path);
        if (!fl.exists())
            throw new IllegalArgumentException();

        startParsing(games, factory.createParser(fl));

        //Logger.getInstance().info("Успешное завершение парсинга Json.");
        return games;
    }

    //region parseJson private methods

    private void startParsing(final JsonUpperClass games, final JsonParser parser) throws IOException {
        parser.nextToken();
        parser.nextToken();

        if (parser.nextToken() != JsonToken.START_ARRAY)
            throw new IllegalArgumentException("Неверная структура файла");

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (parser.getCurrentName() == null) {
                continue;
            }

            switch (parser.getCurrentName()) {
                case "name" -> getName(games, parser);
                case "year" -> getYear(games, parser);
                case "gamePublisher" -> getGamePublisher(games, parser);
                case "platforms" -> getPlatforms(games, parser);
                case "devStudios" -> getDevStudios(games, parser);
                default -> throw new IllegalArgumentException("Неверная структура файла");
            }
        }
    }

    private static void getName(final JsonUpperClass games, final JsonParser parser) throws IOException {
        games.addGame("place_holder", -1, "place_holder");
        parser.nextToken();
        games.returnLastGame().setName(parser.getText());
    }

    private static void getYear(final JsonUpperClass games, final JsonParser parser) throws IOException {
        parser.nextToken();
        games.returnLastGame().setYear(Integer.parseInt(parser.getText()));
    }

    private static void getGamePublisher(final JsonUpperClass games, final JsonParser parser) throws IOException {
        parser.nextToken();
        games.returnLastGame().setGamePublisher(parser.getText());
    }

    private static void getPlatforms(final JsonUpperClass games, final JsonParser parser) throws IOException {
        parser.nextToken();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            parser.nextToken();
            if (parser.getCurrentName().equals("name")) {
                parser.nextToken();
                games.returnLastGame().addPlatform(parser.getText());
                parser.nextToken();
            }
        }
    }

    private static void getDevStudios(final JsonUpperClass games, final JsonParser parser) throws IOException {
        parser.nextToken();
        parser.nextToken();

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (parser.getCurrentName() == null) {
                continue;
            }

            if (parser.getCurrentName().equals("name")) {
                parser.nextToken();
                games.returnLastGame().addDevStudio(parser.getText(), -1, "place_holder");
            } else if (parser.getCurrentName().equals("yearOfFoundation")) {
                parser.nextToken();
                games.returnLastGame().returnLastDevStudio()
                        .setYearOfFoundation(Integer.parseInt(parser.getText()));
            } else if (parser.getCurrentName().equals("url")) {
                parser.nextToken();
                games.returnLastGame().returnLastDevStudio().setUrl(parser.getText());
            }
        }
    }

    //endregion

    /**
     * Конвертирует Json классы данных в XML классы.
     *
     * @return класс, содержащий данные подобно Json файлу.
     * @throws IllegalArgumentException в случае передачи параметром null.
     */
    public XmlUpperClass convert(final JsonUpperClass games) throws IllegalArgumentException {
        //Logger.getInstance().info("Начало конвертирования классов");

        if (games == null)
            throw new IllegalArgumentException();

        final XmlUpperClass gameIndustry = new XmlUpperClass();

        startConvert(games, gameIndustry);


        //Logger.getInstance().info("Конвертирование классов прошло успешно");
        return gameIndustry;
    }

    //region Convert private methods

    private void startConvert(final JsonUpperClass games, final XmlUpperClass gameIndustry) {
        gameIndustry.addPublisher(games.getGames().get(0).getGamePublisher());

        for (int index = 0; index < games.returnLength(); index++) {
            convertDevStudios(games.getGames().get(index),
                    findPublisher(games.getGames().get(index), gameIndustry),
                    collectPlatforms(games.getGames().get(index)));
        }
    }

    private static ArrayList<String> collectPlatforms(final JsonGame jsonGame) {
        final ArrayList<String> XmlPlatforms = new ArrayList<>();
        for (int index = 0; index < jsonGame.getPlatforms().size(); index++) {
            XmlPlatforms.add(jsonGame.getPlatforms().get(index).getName());
        }
        return XmlPlatforms;
    }

    private void convertDevStudios(final JsonGame jsonGame, final XmlGamePublisher XmlPublisher,
                                   final ArrayList<String> XmlPlatforms) {
        for (int index = 0; index < jsonGame.getDevStudios().size(); index++) {

            findDev(jsonGame.getDevStudios().get(index), XmlPublisher)
                    .addGame(jsonGame.getName(), jsonGame.getYear());

            for (String xmlPlatform : XmlPlatforms) {
                findDev(jsonGame.getDevStudios().get(index), XmlPublisher)
                        .getGames()
                        .get(findDev(jsonGame.getDevStudios().get(index), XmlPublisher).returnLength() - 1)
                        .addPlatform(xmlPlatform);
            }
        }
    }

    private XmlDevStudio findDev(final JsonDevStudio devStudio, final XmlGamePublisher publisher) {
        List<XmlDevStudio> devs = publisher.getDevStudios();
        for (int index = devs.size() - 1; index >= 0; index--) {
            if (devs.get(index).getName().equals(devStudio.getName())) {
                return devs.get(index);
            }
        }

        publisher.addDevStudio(devStudio.getName(),
                devStudio.getYearOfFoundation(), devStudio.getUrl());

        return publisher.getDevStudios().get(publisher.returnLength() - 1);
    }

    private XmlGamePublisher findPublisher(final JsonGame jsonGame, final XmlUpperClass xml) {
        List<XmlGamePublisher> gamePublishers = xml.getPublishers();
        for (int index = gamePublishers.size() - 1; index >= 0; index--) {
            if (gamePublishers.get(index).getName().equals(jsonGame.getGamePublisher())) {
                return gamePublishers.get(index);
            }
        }

        xml.addPublisher(jsonGame.getGamePublisher());


        return xml.getPublishers().get(xml.returnLength() - 1);
    }

    //endregion

    /**
     * Запускает создание файла.
     *
     * @param xmlUpperClassClass класс, содержащий данные для Xml файла
     *                           (заполняется в методе convert).
     * @param path               абсолютный путь к новому Xml файлу.
     */
    public void createXML(final XmlUpperClass xmlUpperClassClass, final String path)
            throws FileNotFoundException, XMLStreamException {
        //Logger.getInstance().info("Начало создания файла XML");

        writeXml(new FileOutputStream(path), xmlUpperClassClass);

        //Logger.getInstance().info("Создание файла прошло успешно");
    }

    //region createXml private methods
    private void writeXml(final OutputStream out, final XmlUpperClass xmlUpperClassClass) throws XMLStreamException {
        final XMLStreamWriter writer = output.createXMLStreamWriter(out);

        startWriting(xmlUpperClassClass, writer);

        writer.flush();
        writer.close();
    }

    private static void startWriting(final XmlUpperClass xmlUpperClassClass,
                                     final XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartDocument("utf-8", "1.0");

        writer.writeStartElement("GameIndustry");
        writer.writeStartElement("gamePublishers");

        writeGamePublishers(xmlUpperClassClass, writer);

        writer.writeEndElement();//конец publishers
        writer.writeEndElement();//конец GameIndustry
    }

    private static void writeGamePublishers(final XmlUpperClass xmlUpperClassClass,
                                            final XMLStreamWriter writer) throws XMLStreamException {
        for (int index = 0; index < xmlUpperClassClass.returnLength(); index++) {
            writer.writeStartElement("gamePublisher");
            writer.writeAttribute("name", xmlUpperClassClass.getPublishers().get(index).getName());

            writer.writeStartElement("developerStudios");

            writeDevStudios(xmlUpperClassClass.getPublishers().get(index), writer);

            writer.writeEndElement();//конец devs
            writer.writeEndElement();//конец publisher
        }
    }

    private static void writeDevStudios(final XmlGamePublisher xmlDevStudio,
                                        final XMLStreamWriter writer) throws XMLStreamException {
        for (int index = 0; index < xmlDevStudio.getDevStudios().size(); index++) {
            writer.writeStartElement("developerStudio");
            writer.writeAttribute("name", xmlDevStudio.getDevStudios().get(index).getName());
            writer.writeAttribute("year_of_foundation",
                    ((Integer) xmlDevStudio.getDevStudios().get(index).getYearOfFoundation()).toString());
            writer.writeAttribute("URL", xmlDevStudio.getDevStudios().get(index).getUrl());

            writer.writeStartElement("games");

            writeGames(writer, xmlDevStudio.getDevStudios().get(index));

            writer.writeEndElement();//конец games
            writer.writeEndElement();//конец dev
        }
    }

    private static void writeGames(final XMLStreamWriter writer, final XmlDevStudio dev) throws XMLStreamException {
        for (int index = 0; index < dev.getGames().size(); index++) {
            writer.writeStartElement("game");
            writer.writeAttribute("name", dev.getGames().get(index).getName());
            writer.writeAttribute("year",
                    ((Integer) dev.getGames().get(index).getYear()).toString());

            writer.writeStartElement("platforms");

            writePlatforms(writer, dev.getGames().get(index));
            writer.writeEndElement();//конец platforms
            writer.writeEndElement();//конец game
        }
    }

    private static void writePlatforms(final XMLStreamWriter writer, final XmlGame game) throws XMLStreamException {
        for (int index = 0; index < game.getPlatforms().size(); index++) {
            writer.writeStartElement("platform");
            writer.writeAttribute("name", game.getPlatforms().get(index).getName());
            writer.writeEndElement();
        }
    }
    //endregion
}
