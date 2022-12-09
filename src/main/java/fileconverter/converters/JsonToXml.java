package fileconverter.converters;

import fileconverter.bean.json.JsonDevStudio;
import fileconverter.bean.json.JsonGame;
import fileconverter.bean.json.JsonUpperClass;
import fileconverter.bean.xml.XmlDevStudio;
import fileconverter.bean.xml.XmlGamePublisher;
import fileconverter.bean.xml.XmlUpperClass;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

/**
 * Конвертирует Json классы данных в XML классы.
 */
@Log4j2
public class JsonToXml implements Converter<JsonUpperClass, XmlUpperClass> {

    /**
     * Конвертирует Json классы данных в XML классы.
     *
     * @param upperClass класс данных Json файла.
     * @return класс, содержащий данные подобно Json файлу.
     * @throws IllegalArgumentException в случае передачи параметром null.
     */
    @Override
    public XmlUpperClass convert(@NonNull JsonUpperClass upperClass) throws IllegalArgumentException {
        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Начало конвертирования классов");

        final XmlUpperClass gameIndustry = new XmlUpperClass();

        startConvert(upperClass, gameIndustry);

        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Конвертирование классов прошло успешно");
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

    private ArrayList<String> collectPlatforms(final JsonGame jsonGame) {
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

            addPlatform(jsonGame, XmlPublisher, XmlPlatforms, index);
        }
    }

    private void addPlatform(JsonGame jsonGame, XmlGamePublisher XmlPublisher, ArrayList<String> XmlPlatforms, int index) {
        for (val xmlPlatform : XmlPlatforms) {
            findDev(jsonGame.getDevStudios().get(index), XmlPublisher)
                .getGames()
                .get(findDev(jsonGame.getDevStudios().get(index), XmlPublisher).returnLength() - 1)
                .addPlatform(xmlPlatform);
        }
    }

    private XmlDevStudio findDev(final JsonDevStudio devStudio, final XmlGamePublisher publisher) {
        for (int index = publisher.getDevStudios().size() - 1; index >= 0; index--) {
            if (publisher.getDevStudios().get(index).getName().equals(devStudio.getName())) {
                return publisher.getDevStudios().get(index);
            }
        }

        publisher.addDevStudio(devStudio.getName(),
            devStudio.getYearOfFoundation(),
            devStudio.getUrl());

        return publisher.getDevStudios().get(publisher.returnLength() - 1);
    }

    private XmlGamePublisher findPublisher(final JsonGame jsonGame, final XmlUpperClass xml) {
        for (int index = xml.getPublishers().size() - 1; index >= 0; index--) {
            if (xml.getPublishers().get(index).getName().equals(jsonGame.getGamePublisher())) {
                return xml.getPublishers().get(index);
            }
        }

        xml.addPublisher(jsonGame.getGamePublisher());
        return xml.getPublishers().get(xml.returnLength() - 1);
    }

    //endregion
}
