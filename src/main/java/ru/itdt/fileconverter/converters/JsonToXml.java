package ru.itdt.fileconverter.converters;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import ru.itdt.fileconverter.bean.json.JsonDevStudio;
import ru.itdt.fileconverter.bean.json.JsonGame;
import ru.itdt.fileconverter.bean.json.JsonPlatform;
import ru.itdt.fileconverter.bean.json.JsonRoot;
import ru.itdt.fileconverter.bean.xml.*;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class JsonToXml {

    /**
     * Конвертирует Json классы данных в XML классы.
     *
     * @param upperClass класс данных Json файла.
     * @return класс, содержащий данные подобно Json файлу.
     */
    public XmlRoot convert(final JsonRoot upperClass) {
        if (log.isDebugEnabled()) {
            log.debug("Начало конвертирования Json в Xml");
        }
        return startConvert(upperClass);
    }

    //region Приватные методы конвертера
    private XmlRoot startConvert(final JsonRoot games) {
        val gameIndustry = new XmlRoot();

        gameIndustry.getPublishers()
            .add(
                new XmlGamePublisher(games.getGames()
                    .get(0)
                    .getGamePublisher()));

        for (JsonGame game : games.getGames()) {
            convertDevStudios(game,
                findPublisher(game, gameIndustry),
                collectPlatforms(game));
        }

        if (log.isDebugEnabled()) {
            log.debug("Конвертирование классов прошло успешно");
        }

        return gameIndustry;
    }

    private List<String> collectPlatforms(final JsonGame jsonGame) {
        val xmlPlatforms = new ArrayList<String>();
        for (JsonPlatform platform : jsonGame.getPlatforms()) {
            xmlPlatforms.add(platform.getName());
        }
        return xmlPlatforms;
    }

    private void convertDevStudios(final JsonGame jsonGame, final XmlGamePublisher xmlGamePublisher,
                                   final List<String> xmlPlatforms) {
        for (JsonDevStudio devStudio : jsonGame.getDevStudios()) {
            findDev(devStudio, xmlGamePublisher)
                .getGames()
                .add(XmlGame.builder()
                    .name(jsonGame.getName())
                    .year(jsonGame.getYear())
                    .build());

            addPlatform(devStudio,
                xmlGamePublisher,
                xmlPlatforms);
        }
    }

    private void addPlatform(final JsonDevStudio devStudio, final XmlGamePublisher xmlGamePublisher,
                             final List<String> xmlPlatforms) {
        for (val xmlPlatform : xmlPlatforms) {
            findDev(devStudio,
                xmlGamePublisher)
                .getGames()
                .get(
                    findDev(devStudio, xmlGamePublisher)
                        .getGames().size() - 1)
                .getPlatforms()
                .add(new XmlPlatform(xmlPlatform));
        }
    }

    private XmlDevStudio findDev(final JsonDevStudio devStudio, final XmlGamePublisher publisher) {
        for (int index = publisher.getDevStudios().size() - 1; index >= 0; index--) {
            if (publisher.getDevStudios().get(index).getName().equals(devStudio.getName())) {
                return publisher.getDevStudios().get(index);
            }
        }

        publisher.getDevStudios()
            .add(XmlDevStudio.builder()
                .name(devStudio.getName())
                .yearOfFoundation(devStudio.getYearOfFoundation())
                .url(devStudio.getUrl())
                .build());

        return publisher.getDevStudios()
            .get(publisher.getDevStudios()
                .size() - 1);
    }

    private XmlGamePublisher findPublisher(final JsonGame jsonGame, final XmlRoot xml) {
        for (XmlGamePublisher publisher : xml.getPublishers()) {
            if (publisher.getName().equals(jsonGame.getGamePublisher())) {
                return publisher;
            }
        }

        xml.getPublishers()
            .add(new XmlGamePublisher(jsonGame.getGamePublisher()));

        return xml.getPublishers()
            .get(xml.getPublishers()
                .size() - 1);
    }
    //endregion
}
