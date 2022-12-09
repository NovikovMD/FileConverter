package fileconverter.converters;

import fileconverter.bean.json.JsonGame;
import fileconverter.bean.json.JsonUpperClass;
import fileconverter.bean.xml.XmlDevStudio;
import fileconverter.bean.xml.XmlGame;
import fileconverter.bean.xml.XmlGamePublisher;
import fileconverter.bean.xml.XmlUpperClass;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Конвертирует Xml класс данных в Json.
 */
@Log4j2
public class XmlToJson implements Converter<XmlUpperClass, JsonUpperClass> {
    /**
     * Конвертирует Xml классы данных в Json классы.
     *
     * @param upperClass класс данных Xml файла.
     * @return класс, содержащий данные подобно Json файлу.
     * @throws IllegalArgumentException в случае передачи параметром null.
     */
    @Override
    public JsonUpperClass convert(@NonNull XmlUpperClass upperClass) throws IllegalArgumentException {
        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Начало конвертирования классов");

        final JsonUpperClass jsonUpperClassGames = new JsonUpperClass();

        startConvert(upperClass, jsonUpperClassGames);

        if (log.isEnabled(Level.DEBUG))
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
        } catch (NullPointerException exception) {
            log.log(Level.WARN, "Получен null при конвертации классов.");
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
}
