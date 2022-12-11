package fileconverter.writers.xml;

import fileconverter.bean.xml.XmlDevStudio;
import fileconverter.bean.xml.XmlGame;
import fileconverter.bean.xml.XmlGamePublisher;
import fileconverter.bean.xml.XmlUpperClass;
import fileconverter.writers.Writer;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;

/**
 * Создает XML файл, используя StAX.
 */
@Log4j2
public class StaxWriter implements Writer<XmlUpperClass> {
    private final XMLOutputFactory output = XMLOutputFactory.newInstance();

    /**
     * Запускает создание Xml файла.
     *
     * @param upperClass класс, содержащий данные для Xml файла
     *                   (заполняется в методе convert).
     * @param stream     приёмник данных для Xml файла.
     * @throws XMLStreamException если произошла ошибка при заполнении файла.
     */
    @Override
    public void write(XmlUpperClass upperClass, OutputStream stream) throws XMLStreamException {
        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Начало создания файла XML");

        writeXml(stream, upperClass);

        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Создание файла прошло успешно");
    }

    //region createXml private methods
    private void writeXml(final OutputStream out, final XmlUpperClass xmlUpperClassClass) throws XMLStreamException {
        final XMLStreamWriter writer = output.createXMLStreamWriter(out);

        startWriting(xmlUpperClassClass, writer);

        writer.flush();
        writer.close();
    }

    private void startWriting(final XmlUpperClass xmlUpperClassClass,
                              final XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartDocument("utf-8", "1.0");

        writer.writeStartElement("GameIndustry");
        writer.writeStartElement("gamePublishers");

        writeGamePublishers(xmlUpperClassClass, writer);

        writer.writeEndElement();//конец publishers
        writer.writeEndElement();//конец GameIndustry
    }

    private void writeGamePublishers(final XmlUpperClass xmlUpperClassClass,
                                     final XMLStreamWriter writer) throws XMLStreamException {
        for (int index = 0; index < xmlUpperClassClass.returnLength(); index++) {
            writer.writeStartElement("gamePublisher");

            writer.writeAttribute("name", xmlUpperClassClass.getPublishers().get(index).getName());

            writer.writeStartElement("developerStudios");

            writeDevStudios(xmlUpperClassClass.getPublishers().get(index), writer);

            writer.writeEndElement();//конец devs
            writer.writeEndElement();//конец publisher
        }
    }

    private void writeDevStudios(final XmlGamePublisher xmlDevStudio,
                                 final XMLStreamWriter writer) throws XMLStreamException {
        for (int index = 0; index < xmlDevStudio.getDevStudios().size(); index++) {
            writer.writeStartElement("developerStudio");

            writer.writeAttribute("name", xmlDevStudio.getDevStudios().get(index).getName());
            writer.writeAttribute("year_of_foundation",
                String.valueOf((xmlDevStudio.getDevStudios()
                    .get(index)
                    .getYearOfFoundation())));
            writer.writeAttribute("URL", xmlDevStudio.getDevStudios().get(index).getUrl());

            writer.writeStartElement("games");

            writeGames(writer, xmlDevStudio.getDevStudios().get(index));

            writer.writeEndElement();//конец games
            writer.writeEndElement();//конец dev
        }
    }

    private void writeGames(final XMLStreamWriter writer, final XmlDevStudio dev) throws XMLStreamException {
        for (int index = 0; index < dev.getGames().size(); index++) {
            writer.writeStartElement("game");

            writer.writeAttribute("name", dev.getGames().get(index).getName());
            writer.writeAttribute("year",
                String.valueOf((dev.getGames().get(index).getYear())));

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
