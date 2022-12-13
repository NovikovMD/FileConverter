package fileconverter.converters;

import fileconverter.bean.json.JsonDevStudio;
import fileconverter.bean.json.JsonGame;
import fileconverter.bean.json.JsonUpper;
import fileconverter.bean.xml.XmlDevStudio;
import fileconverter.bean.xml.XmlGamePublisher;
import fileconverter.bean.xml.XmlUpper;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.util.ArrayList;

/**
 * Конвертирует Json классы данных в XML классы.
 */
@Log4j2
public class JsonToXml implements Converter<JsonUpper, XmlUpper> {

    /**
     * Конвертирует Json классы данных в XML классы.
     *
     * @param upperClass класс данных Json файла.
     * @return класс, содержащий данные подобно Json файлу.
     * @throws IllegalArgumentException в случае передачи параметром null.
     */
    @Override
    public XmlUpper convert(@NonNull final JsonUpper upperClass) throws IllegalArgumentException {
        if (log.isDebugEnabled()) {
            log.debug("Начало конвертирования Json в Xml");
        }

        val gameIndustry = new XmlUpper();

        startConvert(upperClass, gameIndustry);

        if (log.isDebugEnabled()) {
            log.debug("Конвертирование классов прошло успешно");
        }
        return gameIndustry;
    }

    //region Convert private methods
    private void startConvert(final JsonUpper games, final XmlUpper gameIndustry) {
        gameIndustry.addPublisher(games.getGames().get(0).getGamePublisher());

        for (int index = 0; index < games.returnLength(); index++) {
            convertDevStudios(games.getGames().get(index),
                findPublisher(games.getGames().get(index), gameIndustry),
                collectPlatforms(games.getGames().get(index)));
        }
    }

    private ArrayList<String> collectPlatforms(final JsonGame jsonGame) {
        val XmlPlatforms = new ArrayList<String>();
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

            addPlatform(jsonGame.getDevStudios().get(index),
                XmlPublisher,
                XmlPlatforms);
        }
    }

    private void addPlatform(final JsonDevStudio devStudio, final XmlGamePublisher XmlPublisher,
                             final ArrayList<String> XmlPlatforms) {
        for (val xmlPlatform : XmlPlatforms) {
            findDev(devStudio,
                XmlPublisher)
                .getGames()
                .get(
                    findDev(devStudio, XmlPublisher)
                        .returnLength() - 1)
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

        return publisher.getDevStudios()
            .get(publisher.returnLength() - 1);
    }

    private XmlGamePublisher findPublisher(final JsonGame jsonGame, final XmlUpper xml) {
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
