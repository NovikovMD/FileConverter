/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package ru.itdt.fileconverter.writers.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itdt.fileconverter.bean.json.JsonRoot;
import ru.itdt.fileconverter.writers.Writer;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Создает Json файл используя Jackson-databind.
 */
@Log4j2
public class JacksonWriter implements Writer<JsonRoot> {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Создает Json файл.
     *
     * @param data    класс, содержащий данные для Json файла.
     * @param newFile путь к новому файлу.
     * @throws IOException если произошла ошибка записи в файл.
     */
    @Override
    public void write(final JsonRoot data, final String newFile) throws IOException {
        if (log.isEnabled(Level.DEBUG)) {
            log.log(Level.DEBUG, "Начало создания файла Jackson");
        }

        try (final FileOutputStream stream = new FileOutputStream(newFile)) {
            mapper.writeValue(stream, data);
        }

        if (log.isEnabled(Level.DEBUG)) {
            log.log(Level.DEBUG, "Создание файла Json прошло успешно");
        }
    }
}
