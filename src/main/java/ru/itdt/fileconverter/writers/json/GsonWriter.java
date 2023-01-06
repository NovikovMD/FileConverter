package ru.itdt.fileconverter.writers.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.itdt.fileconverter.bean.json.JsonRoot;
import ru.itdt.fileconverter.writers.Writer;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Создает Json файл используя Gson.
 */
@Log4j2
public class GsonWriter implements Writer<JsonRoot> {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Создает Json файл.
     *
     * @param data    класс, содержащий данные для Json файла.
     * @param newFile путь к новому файлу.
     * @throws IOException если произошла ошибка записи в файл.
     */
    @Override
    public void write(final JsonRoot data, final String newFile) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Начало создания файла Gson");
        }

        try (final BufferedWriter bufferedWriter =  new BufferedWriter(new FileWriter(newFile))) {
            gson.toJson(data, bufferedWriter);
        }

        if (log.isDebugEnabled()) {
            log.debug("Создание файла Json прошло успешно");
        }
    }
}
