package fileconverter.converters;

import fileconverter.bean.json.JsonGame;
import fileconverter.bean.json.JsonUpper;
import fileconverter.bean.xml.XmlDevStudio;
import fileconverter.bean.xml.XmlGame;
import fileconverter.bean.xml.XmlGamePublisher;
import fileconverter.bean.xml.XmlUpper;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.util.ArrayList;

/**
 * Конвертирует Xml класс данных в Json.
 */
@Log4j2
public class XmlToJson implements Converter<XmlUpper, JsonUpper> {
    /**
     * Конвертирует Xml классы данных в Json классы.
     *
     * @param upperClass класс данных Xml файла.
     * @return класс, содержащий данные подобно Json файлу.
     * @throws IllegalArgumentException в случае передачи параметром null.
     */
    @Override
    public JsonUpper convert(@NonNull final XmlUpper upperClass) throws IllegalArgumentException {
        if (log.isDebugEnabled()) {
            log.debug("Начало конвертирования Xml в Json");
        }

        val jsonUpperClassGames = new JsonUpper();

        startConvert(upperClass, jsonUpperClassGames);

        if (log.isDebugEnabled()) {
            log.debug("Конвертирование классов прошло успешно");
        }
        return jsonUpperClassGames;
    }

    //region Convert private methods
    private void startConvert(final XmlUpper gameIndustry, final JsonUpper jsonUpperGames) {
        for (int index = 0; index < gameIndustry.returnLength(); index++) {
            getPublisher(jsonUpperGames, gameIndustry.getPublishers().get(index));
        }
    }

    private void getPublisher(final JsonUpper jsonUpperGames, final XmlGamePublisher publisher) {
        for (int index = 0; index < publisher.returnLength(); index++) {
            getDeveloper(jsonUpperGames, publisher, publisher.getDevStudios().get(index));
        }
    }

    private void getDeveloper(final JsonUpper jsonUpperGames, final XmlGamePublisher publisher,
                              final XmlDevStudio developer) {
        for (int index = 0; index < developer.returnLength(); index++) {
            getGame(jsonUpperGames, publisher, developer, developer.getGames().get(index));
        }
    }

    private void getGame(final JsonUpper jsonUpperGames, final XmlGamePublisher publisher,
                         final XmlDevStudio developer, final XmlGame game) {
        if (getCurrentGame(game.getName(), jsonUpperGames.getGames()) == null) {
            createNewGame(jsonUpperGames, publisher, developer, game);
            return;
        }
        //не будет null. Проверяется выше.
        getCurrentGame(game.getName(), jsonUpperGames.getGames())
                .addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());

    }

    private JsonGame getCurrentGame(final String nameToFind, final ArrayList<JsonGame> listToLookIn) {
        for (val jsonGame : listToLookIn) {
            if (jsonGame.getName().equals(nameToFind)) {
                return jsonGame;
            }
        }
        return null;
    }

    private void createNewGame(final JsonUpper jsonUpperGames, final XmlGamePublisher publisher,
                               final XmlDevStudio developer, final XmlGame game) {
        jsonUpperGames.addGame(game.getName(), game.getYear(), publisher.getName());

        getPlatform(game, jsonUpperGames.getGames().get(jsonUpperGames.returnLength() - 1));

        jsonUpperGames.getGames().get(jsonUpperGames.returnLength() - 1)
            .addDevStudio(developer.getName(), developer.getYearOfFoundation(), developer.getUrl());
    }

    private void getPlatform(final XmlGame game, final JsonGame jsonGame) {
        for (int index = 0; index < game.returnLength(); index++) {
            jsonGame.addPlatform(game.getPlatforms().get(index).getName());
        }
    }
    //endregion
}
