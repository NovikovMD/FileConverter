package fileconverter.readers.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import fileconverter.bean.json.JsonUpperClass;
import fileconverter.readers.Reader;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.InputStream;

/**
 * Создает Json файл используя Jackson-databind.
 */
@Log4j2
public class JacksonReader implements Reader<JsonUpperClass> {
    private final JsonFactory factory = new JsonFactory();

    /**
     * Считывает данные из Json файла.
     *
     * @param stream источник Json файла.
     * @return класс, содержащий данные из исходного Json файла.
     * @throws IOException              если считывание Json файла было прервано.
     * @throws IllegalArgumentException если передан неверный путь к Json файлу
     *                                  или некорректная структура файла.
     */
    @Override
    public JsonUpperClass parse(final InputStream stream) throws IOException {
        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Начало работы парсера Jackson");
        final JsonUpperClass games = new JsonUpperClass();

        startParsing(games, factory.createParser(stream));

        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Успешное завершение парсинга Json.");
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

    private void getName(final JsonUpperClass games, final JsonParser parser) throws IOException {
        games.addGame("place_holder", -1, "place_holder");
        parser.nextToken();
        games.returnLastGame()
            .setName(parser.getText());
    }

    private void getYear(final JsonUpperClass games, final JsonParser parser) throws IOException {
        parser.nextToken();
        games.returnLastGame()
            .setYear(Integer.parseInt(parser.getText()));
    }

    private void getGamePublisher(final JsonUpperClass games, final JsonParser parser) throws IOException {
        parser.nextToken();
        games.returnLastGame()
            .setGamePublisher(parser.getText());
    }

    private void getPlatforms(final JsonUpperClass games, final JsonParser parser) throws IOException {
        parser.nextToken();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            parser.nextToken();
            if (parser.getCurrentName().equals("name")) {
                parser.nextToken();
                games.returnLastGame()
                    .addPlatform(parser.getText());
                parser.nextToken();
            }
        }
    }

    private void getDevStudios(final JsonUpperClass games, final JsonParser parser) throws IOException {
        parser.nextToken();
        parser.nextToken();

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (parser.getCurrentName() == null) {
                continue;
            }

            if (parser.getCurrentName().equals("name")) {
                parser.nextToken();
                games.returnLastGame()
                    .addDevStudio(parser.getText(),
                        -1,
                        "place_holder");
                continue;
            }
            if (parser.getCurrentName().equals("yearOfFoundation")) {
                parser.nextToken();
                games.returnLastGame()
                    .returnLastDevStudio()
                    .setYearOfFoundation(
                        Integer.parseInt(parser.getText()));
                continue;
            }
            if (parser.getCurrentName().equals("url")) {
                parser.nextToken();
                games.returnLastGame()
                    .returnLastDevStudio()
                    .setUrl(parser.getText());
            }
        }
    }
    //endregion
}
