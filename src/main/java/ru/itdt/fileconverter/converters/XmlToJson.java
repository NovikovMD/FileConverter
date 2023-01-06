package ru.itdt.fileconverter.converters;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import ru.itdt.fileconverter.bean.json.JsonDevStudio;
import ru.itdt.fileconverter.bean.json.JsonGame;
import ru.itdt.fileconverter.bean.json.JsonPlatform;
import ru.itdt.fileconverter.bean.json.JsonRoot;
import ru.itdt.fileconverter.bean.xml.*;

import java.util.List;

@Log4j2
public class XmlToJson {
    /**
     * Конвертирует Xml классы данных в Json классы.
     *
     * @param upperClass класс данных Xml файла.
     * @return класс, содержащий данные подобно Json файлу.
     */
    public JsonRoot convert(final XmlRoot upperClass) {
        if (log.isDebugEnabled()) {
            log.debug("Начало конвертирования Xml в Json");
        }
        return startConvert(upperClass);
    }

    //region Приватные методы конвертера
    private JsonRoot startConvert(final XmlRoot gameIndustry) {
        val jsonRoot = new JsonRoot();
        for (XmlGamePublisher publisher : gameIndustry.getPublishers()) {
            getPublisher(jsonRoot, publisher);
        }

        if (log.isDebugEnabled()) {
            log.debug("Конвертирование классов прошло успешно");
        }

        return jsonRoot;
    }

    private void getPublisher(final JsonRoot jsonRootGames, final XmlGamePublisher publisher) {
        for (XmlDevStudio devStudio : publisher.getDevStudios()) {
            getDeveloper(jsonRootGames, publisher, devStudio);
        }
    }

    private void getDeveloper(final JsonRoot jsonRootGames, final XmlGamePublisher publisher,
                              final XmlDevStudio developer) {
        for (XmlGame game : developer.getGames()) {
            getGame(jsonRootGames, publisher, developer, game);
        }
    }

    private void getGame(final JsonRoot jsonRootGames, final XmlGamePublisher publisher,
                         final XmlDevStudio developer, final XmlGame game) {
        val currentGame = getCurrentGame(game.getName(), jsonRootGames.getGames());
        if (currentGame == null) {
            createNewGame(jsonRootGames, publisher, developer, game);
            return;
        }
        currentGame.getDevStudios()
            .add(JsonDevStudio.builder()
                .name(developer.getName())
                .yearOfFoundation(developer.getYearOfFoundation())
                .url(developer.getUrl())
                .build());
    }

    private JsonGame getCurrentGame(final String nameToFind, final List<JsonGame> listToLookIn) {
        return listToLookIn.stream()
            .filter(jsonGame -> jsonGame.getName().equals(nameToFind))
            .findFirst()
            .orElse(null);
    }

    private void createNewGame(final JsonRoot jsonRootGames, final XmlGamePublisher publisher,
                               final XmlDevStudio developer, final XmlGame game) {
        jsonRootGames.getGames()
            .add(JsonGame.builder()
                .name(game.getName())
                .year(game.getYear())
                .gamePublisher(publisher.getName())
                .build());

        getPlatform(game, jsonRootGames.getGames()
            .get(jsonRootGames.getGames()
                .size() - 1));

        jsonRootGames.getGames()
            .get(jsonRootGames.getGames()
                .size() - 1)
            .getDevStudios()
            .add(JsonDevStudio.builder()
                .name(developer.getName())
                .yearOfFoundation(developer.getYearOfFoundation())
                .url(developer.getUrl())
                .build());
    }

    private void getPlatform(final XmlGame game, final JsonGame jsonGame) {
        for (XmlPlatform platform : game.getPlatforms()) {
            jsonGame.getPlatforms()
                .add(new JsonPlatform(platform.getName()));
        }
    }
    //endregion
}
