/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package fileconverter.writers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fileconverter.bean.json.JsonUpperClass;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Создает Json файл используя Jackson-databind.
 */
@Log4j2
public class JacksonWriter implements Writer<JsonUpperClass> {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Создает Json файл.
     *
     * @param upperClass класс, содержащий данные для Json файла
     *                   (заполняется в методе convert).
     * @param stream     приёмник данных для Json файла.
     * @throws IOException если произошла ошибка записи в файл.
     */
    @Override
    public void write(JsonUpperClass upperClass, OutputStream stream) throws IOException {
        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Начало создания файла JSON");

        mapper.writeValue(stream, upperClass);

        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Создание файла прошло успешно");
    }
}
