package fileconverter.readers.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import fileconverter.bean.json.JsonUpper;
import fileconverter.readers.Reader;
import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Создает Json файл используя Jackson-databind.
 */
@Log4j2
public class JacksonReader implements Reader<JsonUpper> {
    private final JsonFactory factory = new JsonFactory();

    /**
     * Считывает данные из Json файла.
     *
     * @param file путь к существующему Json файлу.
     * @return класс, содержащий данные из исходного Json файла.
     * @throws IOException              если считывание Json файла было прервано.
     * @throws IllegalArgumentException если передан неверный путь к Json файлу
     *                                  или некорректная структура файла.
     */
    @Override
    public JsonUpper parse(final String file) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Начало работы парсера Jackson");
        }
        final JsonUpper games = new JsonUpper();

        try (final FileInputStream stream = new FileInputStream(file)) {
            startParsing(games, factory.createParser(stream));
        }

        if (log.isDebugEnabled()) {
            log.debug("Успешное завершение парсинга Json.");
        }
        return games;
    }

    //region parseJson private methods
    private void startParsing(final JsonUpper games, final JsonParser parser) throws IOException {
        parser.nextToken();
        parser.nextToken();

        if (parser.nextToken() != JsonToken.START_ARRAY)
            throw new IllegalArgumentException("Неверная структура файла");

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (parser.getCurrentName() == null) {
                continue;
            }

            switch (parser.getCurrentName()) {
                case "Название" -> getName(games, parser);
                case "Год выпуска" -> getYear(games, parser);
                case "Издатель" -> getGamePublisher(games, parser);
                case "Платформы" -> getPlatforms(games, parser);
                case "Разработчики" -> getDevStudios(games, parser);
                default -> throw new IllegalArgumentException("Неверная структура файла");
            }
        }
    }

    private void getName(final JsonUpper games, final JsonParser parser) throws IOException {
        games.addGame("place_holder", -1, "place_holder");
        parser.nextToken();
        games.returnLastGame()
            .setName(parser.getText());
    }

    private void getYear(final JsonUpper games, final JsonParser parser) throws IOException {
        parser.nextToken();
        games.returnLastGame()
            .setYear(Integer.parseInt(parser.getText()));
    }

    private void getGamePublisher(final JsonUpper games, final JsonParser parser) throws IOException {
        parser.nextToken();
        games.returnLastGame()
            .setGamePublisher(parser.getText());
    }

    private void getPlatforms(final JsonUpper games, final JsonParser parser) throws IOException {
        parser.nextToken();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            parser.nextToken();
            if (parser.getCurrentName().equals("Название")) {
                parser.nextToken();
                games.returnLastGame()
                    .addPlatform(parser.getText());
                parser.nextToken();
            }
        }
    }

    private void getDevStudios(final JsonUpper games, final JsonParser parser) throws IOException {
        parser.nextToken();
        parser.nextToken();

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (parser.getCurrentName() == null) {
                continue;
            }

            if (parser.getCurrentName().equals("Наименование")) {
                parser.nextToken();
                games.returnLastGame()
                    .addDevStudio(parser.getText(),
                        -1,
                        "place_holder");
                continue;
            }
            if (parser.getCurrentName().equals("Год основания")) {
                parser.nextToken();
                games.returnLastGame()
                    .returnLastDevStudio()
                    .setYearOfFoundation(
                        Integer.parseInt(parser.getText()));
                continue;
            }
            if (parser.getCurrentName().equals("URL")) {
                parser.nextToken();
                games.returnLastGame()
                    .returnLastDevStudio()
                    .setUrl(parser.getText());
            }
        }
    }
    //endregion
}
