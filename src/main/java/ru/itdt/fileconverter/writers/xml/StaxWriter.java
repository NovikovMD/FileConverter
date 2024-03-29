package ru.itdt.fileconverter.writers.xml;

import ru.itdt.fileconverter.bean.xml.XmlDevStudio;
import ru.itdt.fileconverter.bean.xml.XmlGame;
import ru.itdt.fileconverter.bean.xml.XmlGamePublisher;
import ru.itdt.fileconverter.bean.xml.XmlRoot;
import ru.itdt.fileconverter.writers.Writer;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Создает XML файл, используя StAX.
 */
@Log4j2
public class StaxWriter implements Writer<XmlRoot> {
    private final XMLOutputFactory output = XMLOutputFactory.newInstance();

    /**
     * Запускает создание Xml файла.
     *
     * @param data    класс, содержащий данные для Xml файла.
     * @param newFile путь новому файлу.
     * @throws XMLStreamException если произошла ошибка при заполнении файла.
     * @throws IOException        если произошла IO ошибка.
     */
    @Override
    public void write(final XmlRoot data, final String newFile) throws XMLStreamException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("Начало создания файла Stax");
        }

        try (final FileOutputStream stream = new FileOutputStream(newFile)) {
            writeXml(stream, data);
        }

        if (log.isDebugEnabled()) {
            log.debug("Создание файла Xml прошло успешно");
        }
    }

    //region createXml private methods
    private void writeXml(final OutputStream out, final XmlRoot xmlRootClass) throws XMLStreamException {
        val writer = output.createXMLStreamWriter(out);

        startWriting(xmlRootClass, writer);

        writer.close();
    }

    private void startWriting(final XmlRoot xmlRootClass,
                              final XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartDocument("utf-8", "1.0");

        writer.writeStartElement("GameIndustry");
        writer.writeStartElement("gamePublishers");

        writeGamePublishers(xmlRootClass, writer);

        writer.writeEndElement();//конец publishers
        writer.writeEndElement();//конец GameIndustry
    }

    private void writeGamePublishers(final XmlRoot xmlRootClass,
                                     final XMLStreamWriter writer) throws XMLStreamException {
        for (int index = 0; index < xmlRootClass.getPublishers().size(); index++) {
            writer.writeStartElement("gamePublisher");

            writer.writeAttribute("name",
                xmlRootClass.getPublishers()
                    .get(index).getName());

            writer.writeStartElement("developerStudios");

            writeDevStudios(xmlRootClass.getPublishers().get(index), writer);

            writer.writeEndElement();//конец devs
            writer.writeEndElement();//конец publisher
        }
    }

    private void writeDevStudios(final XmlGamePublisher xmlDevStudio,
                                 final XMLStreamWriter writer) throws XMLStreamException {
        for (int index = 0; index < xmlDevStudio.getDevStudios().size(); index++) {
            writer.writeStartElement("developerStudio");

            writer.writeAttribute("name",
                xmlDevStudio.getDevStudios()
                    .get(index).getName());
            writer.writeAttribute("year_of_foundation",
                String.valueOf((xmlDevStudio.getDevStudios()
                    .get(index)
                    .getYearOfFoundation())));
            writer.writeAttribute("URL",
                xmlDevStudio.getDevStudios()
                    .get(index).getUrl());

            writer.writeStartElement("games");

            writeGames(writer, xmlDevStudio.getDevStudios().get(index));

            writer.writeEndElement();//конец games
            writer.writeEndElement();//конец dev
        }
    }

    private void writeGames(final XMLStreamWriter writer, final XmlDevStudio dev) throws XMLStreamException {
        for (int index = 0; index < dev.getGames().size(); index++) {
            writer.writeStartElement("game");

            writer.writeAttribute("name",
                dev.getGames()
                    .get(index).getName());
            writer.writeAttribute("year",
                String.valueOf((dev.getGames()
                    .get(index).getYear())));

            writer.writeStartElement("platforms");

            writePlatforms(writer, dev.getGames().get(index));

            writer.writeEndElement();//конец platforms
            writer.writeEndElement();//конец game
        }
    }

    private void writePlatforms(final XMLStreamWriter writer, final XmlGame game) throws XMLStreamException {
        for (int index = 0; index < game.getPlatforms().size(); index++) {
            writer.writeStartElement("platform");
            writer.writeAttribute("name", game.getPlatforms().get(index).getName());
            writer.writeEndElement();
        }
    }
    //endregion
}
