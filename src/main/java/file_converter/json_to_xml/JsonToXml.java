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
     * @return класс, содержащий даныне из исходного Json файла.
     * @throws IOException если считывание Json файла было прервано.
     */
    public JsonUpperClass parseJson(final String path) throws IOException {
        JsonUpperClass games = new JsonUpperClass();
        JsonParser parser = factory.createParser(new File(path));

        startParsing(games, parser);

        return games;
    }

    //region parseJson private methods

    private void startParsing(final JsonUpperClass games, JsonParser parser) throws IOException {
        parser.nextToken();
        parser.nextToken();

        if (parser.nextToken() != JsonToken.START_ARRAY)
            throw new RuntimeException("Invalid file structure");

        //loop until token equal to "]"
        while (parser.nextToken() != JsonToken.END_ARRAY) {

            //checker if we're looking at "{" / "}"
            if (parser.getCurrentName() == null) {
                continue;
            }

            switch (parser.getCurrentName()) {
                case "name" -> getName(games, parser);
                case "year" -> getYear(games, parser);
                case "gamePublisher" -> getGamePublisher(games, parser);
                case "platforms" -> getPlatforms(games, parser);
                case "devStudios" -> getDevStudios(games, parser);
                default -> throw new RuntimeException("Invalid file structure");
            }
        }
    }

    private static void getName(final JsonUpperClass games, JsonParser parser) throws IOException {
        games.addGame("place_holder", -1, "place_holder");
        parser.nextToken();
        games.returnLastGame().setName(parser.getText());
    }

    private static void getYear(final JsonUpperClass games, JsonParser parser) throws IOException {
        parser.nextToken();
        games.returnLastGame().setYear(Integer.parseInt(parser.getText()));
    }

    private static void getGamePublisher(final JsonUpperClass games, JsonParser parser) throws IOException {
        parser.nextToken();
        games.returnLastGame().setGamePublisher(parser.getText());
    }

    private static void getPlatforms(final JsonUpperClass games, JsonParser parser) throws IOException {
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

    private static void getDevStudios(final JsonUpperClass games, JsonParser parser) throws IOException {
        parser.nextToken();
        parser.nextToken();

        //loop until end of devStudios
        while (parser.nextToken() != JsonToken.END_ARRAY) {

            //checker if we're looking at "{" / "}"
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
     */
    public XmlUpperClass convert(final JsonUpperClass games) {
        XmlUpperClass gameIndustry = new XmlUpperClass();

        startConvert(games, gameIndustry);

        return gameIndustry;
    }

    //region Convert private methods

    private void startConvert(final JsonUpperClass games, final XmlUpperClass gameIndustry) {
        gameIndustry.addPublisher(games.getGames().get(0).getGamePublisher());

        for (int i = 0; i < games.returnLength(); i++) {
            //get current game in json
            JsonGame jsonGame = games.getGames().get(i);

            XmlGamePublisher XmlPublisher = findPublisher(jsonGame, gameIndustry);

            ArrayList<String> XmlPlatforms = collectPlatforms(jsonGame);

            convertDevStudios(jsonGame, XmlPublisher, XmlPlatforms);
        }
    }

    private static ArrayList<String> collectPlatforms(final JsonGame jsonGame) {
        ArrayList<String> XmlPlatforms = new ArrayList<>();
        for (int j = 0; j < jsonGame.getPlatforms().size(); j++) {
            XmlPlatforms.add(jsonGame.getPlatforms().get(j).getName());
        }
        return XmlPlatforms;
    }

    private void convertDevStudios(final JsonGame jsonGame, final XmlGamePublisher XmlPublisher,
                                   final ArrayList<String> XmlPlatforms) {
        for (int j = 0; j < jsonGame.getDevStudios().size(); j++) {
            //get current devStudio in json
            JsonDevStudio jsonDevStudio = jsonGame.getDevStudios().get(j);

            XmlDevStudio xmlDev = findDev(jsonDevStudio, XmlPublisher);

            //add game
            xmlDev.addGame(jsonGame.getName(), jsonGame.getYear());

            //set all platforms
            XmlGame xmlGame = xmlDev.getGames().get(xmlDev.returnLength() - 1);
            for (String xmlPlatform : XmlPlatforms) {
                xmlGame.addPlatform(xmlPlatform);
            }
        }
    }

    private XmlDevStudio findDev(final JsonDevStudio devStudio, final XmlGamePublisher publisher) {
        List<XmlDevStudio> devs = publisher.getDevStudios();
        for (int i = devs.size() - 1; i >= 0; i--) {
            if (devs.get(i).getName().equals(devStudio.getName())) {
                return devs.get(i);
            }
        }
        //create new dev
        publisher.addDevStudio(devStudio.getName(),
                devStudio.getYearOfFoundation(), devStudio.getUrl());

        int index = publisher.returnLength();
        return publisher.getDevStudios().get(index - 1);
    }

    private XmlGamePublisher findPublisher(final JsonGame jsonGame, final XmlUpperClass xml) {
        List<XmlGamePublisher> gamePublishers = xml.getPublishers();
        for (int i = gamePublishers.size() - 1; i >= 0; i--) {
            if (gamePublishers.get(i).getName().equals(jsonGame.getGamePublisher())) {
                return gamePublishers.get(i);
            }
        }

        xml.addPublisher(jsonGame.getGamePublisher());


        int index = xml.returnLength();
        return xml.getPublishers().get(index - 1);
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
        FileOutputStream out = new FileOutputStream(path);
        writeXml(out, xmlUpperClassClass);
    }

    //region createXml private methods
    private void writeXml(final OutputStream out, final XmlUpperClass xmlUpperClassClass) throws XMLStreamException {
        XMLStreamWriter writer = output.createXMLStreamWriter(out);

        startWriting(xmlUpperClassClass, writer);

        writer.flush();
        writer.close();
    }

    private static void startWriting(final XmlUpperClass xmlUpperClassClass,
                                     final XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartDocument("utf-8", "1.0");

        // header
        writer.writeStartElement("GameIndustry");
        writer.writeStartElement("gamePublishers");

        writeGamePublishers(xmlUpperClassClass, writer);

        writer.writeEndElement();//end publishers
        writer.writeEndElement();//end GameIndustry
    }

    private static void writeGamePublishers(final XmlUpperClass xmlUpperClassClass,
                                            final XMLStreamWriter writer) throws XMLStreamException {
        for (int i = 0; i < xmlUpperClassClass.returnLength(); i++) {
            writer.writeStartElement("gamePublisher");
            writer.writeAttribute("name", xmlUpperClassClass.getPublishers().get(i).getName());

            writer.writeStartElement("developerStudios");

            XmlGamePublisher xmlDevStudio = xmlUpperClassClass.getPublishers().get(i);
            writeDevStudios(xmlDevStudio, writer);
            writer.writeEndElement();//end devs
            writer.writeEndElement();//end publisher
        }
    }

    private static void writeDevStudios(final XmlGamePublisher xmlDevStudio,
                                        final XMLStreamWriter writer) throws XMLStreamException {
        for (int j = 0; j < xmlDevStudio.getDevStudios().size(); j++) {
            XmlDevStudio dev = xmlDevStudio.getDevStudios().get(j);

            writer.writeStartElement("developerStudio");
            writer.writeAttribute("name", dev.getName());
            writer.writeAttribute("year_of_foundation", ((Integer) dev.getYearOfFoundation()).toString());
            writer.writeAttribute("URL", dev.getUrl());

            writer.writeStartElement("games");

            writeGames(writer, dev);
            writer.writeEndElement();//end games
            writer.writeEndElement();//end dev
        }
    }

    private static void writeGames(final XMLStreamWriter writer, final XmlDevStudio dev) throws XMLStreamException {
        for (int k = 0; k < dev.getGames().size(); k++) {
            XmlGame game = dev.getGames().get(k);

            writer.writeStartElement("game");
            writer.writeAttribute("name", game.getName());
            writer.writeAttribute("year", ((Integer) game.getYear()).toString());

            writer.writeStartElement("platforms");

            writePlatforms(writer, game);
            writer.writeEndElement();//end platforms
            writer.writeEndElement();//end game
        }
    }

    private static void writePlatforms(final XMLStreamWriter writer, final XmlGame game) throws XMLStreamException {
        for (int l = 0; l < game.getPlatforms().size(); l++) {
            XmlPlatform xmLplatform = game.getPlatforms().get(l);

            writer.writeStartElement("platform");
            writer.writeAttribute("name", xmLplatform.getName());
            writer.writeEndElement();
        }
    }
    //endregion
}
