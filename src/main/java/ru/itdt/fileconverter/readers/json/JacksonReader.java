package ru.itdt.fileconverter.readers.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import ru.itdt.fileconverter.bean.json.JsonDevStudio;
import ru.itdt.fileconverter.bean.json.JsonGame;
import ru.itdt.fileconverter.bean.json.JsonPlatform;
import ru.itdt.fileconverter.bean.json.JsonRoot;
import ru.itdt.fileconverter.readers.Reader;
import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Создает Json файл используя Jackson-databind.
 */
@Log4j2
public class JacksonReader implements Reader<JsonRoot> {
    private static final JsonFactory factory = new JsonFactory();

    /**
     * Считывает данные из Json файла.
     *
     * @param file путь к существующему Json файлу.
     * @return класс, содержащий данные из исходного Json файла.
     * @throws IOException если считывание Json файла было прервано
     *                     или при любой другой IO ошибке.
     */
    @Override
    public JsonRoot parse(final String file) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Начало работы парсера Jackson");
        }
        final JsonRoot games = new JsonRoot();

        try (final FileInputStream stream = new FileInputStream(file)) {
            startParsing(games, factory.createParser(stream));
        }

        if (log.isDebugEnabled()) {
            log.debug("Успешное завершение парсинга Json.");
        }
        return games;
    }

    //region parseJson private methods
    private void startParsing(final JsonRoot games, final JsonParser parser) throws IOException {
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

    private void getName(final JsonRoot games, final JsonParser parser) throws IOException {
        games.getGames().add(JsonGame.builder()
            .name("place_holder")
            .year(-1)
            .gamePublisher("place_holder")
            .build());
        parser.nextToken();
        games.getGames()
            .get(games.getGames().size() - 1)
            .setName(parser.getText());
    }

    private void getYear(final JsonRoot games, final JsonParser parser) throws IOException {
        parser.nextToken();
        games.getGames()
            .get(games.getGames().size() - 1)
            .setYear(Integer.parseInt(parser.getText()));
    }

    private void getGamePublisher(final JsonRoot games, final JsonParser parser) throws IOException {
        parser.nextToken();
        games.getGames()
            .get(games.getGames().size() - 1)
            .setGamePublisher(parser.getText());
    }

    private void getPlatforms(final JsonRoot games, final JsonParser parser) throws IOException {
        parser.nextToken();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            parser.nextToken();
            if (parser.getCurrentName().equals("Название")) {
                parser.nextToken();
                games.getGames()
                    .get(games.getGames().size() - 1)
                    .getPlatforms()
                    .add(
                        new JsonPlatform(parser.getText()));
                parser.nextToken();
            }
        }
    }

    private void getDevStudios(final JsonRoot games, final JsonParser parser) throws IOException {
        parser.nextToken();
        parser.nextToken();

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (parser.getCurrentName() == null) {
                continue;
            }

            if (parser.getCurrentName().equals("Наименование")) {
                parser.nextToken();
                games.getGames()
                    .get(games.getGames().size() - 1)
                    .getDevStudios()
                    .add(JsonDevStudio.builder()
                        .name(parser.getText())
                        .yearOfFoundation(-1)
                        .url("place_holder")
                        .build());
                continue;
            }
            if (parser.getCurrentName().equals("Год основания")) {
                parser.nextToken();
                games.getGames()
                    .get(games.getGames().size() - 1)
                    .getDevStudios()
                    .get(games.getGames()
                        .get(games.getGames().size() - 1)
                        .getDevStudios().size() - 1)
                    .setYearOfFoundation(
                        Integer.parseInt(parser.getText()));
                continue;
            }
            if (parser.getCurrentName().equals("URL")) {
                parser.nextToken();
                games.getGames()
                    .get(games.getGames().size() - 1)
                    .getDevStudios()
                    .get(games.getGames()
                        .get(games.getGames().size() - 1)
                        .getDevStudios().size() - 1)
                    .setUrl(parser.getText());
            }
        }
    }
    //endregion
}
