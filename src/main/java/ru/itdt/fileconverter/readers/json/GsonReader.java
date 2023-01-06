package ru.itdt.fileconverter.readers.json;

import com.google.gson.Gson;
import ru.itdt.fileconverter.bean.json.JsonRoot;
import ru.itdt.fileconverter.readers.Reader;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Создает Json файл используя Gson.
 */
@Log4j2
public class GsonReader implements Reader<JsonRoot> {
    private static final Gson gson = new Gson();

    /**
     * Считывает данные из Json файла.
     *
     * @param file путь к существующему файлу.
     * @return класс, содержащий данные из исходного Json файла.
     * @throws IOException при ошибке создания потока входных данных.
     */
    @Override
    public JsonRoot parse(final String file) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Начало работы парсера Gson");
        }
        try (
            val fileStream = new FileInputStream(file);
            val streamReader = new InputStreamReader(fileStream);
            val bufferedReader = new BufferedReader(streamReader)
        ) {
            return gson.fromJson(bufferedReader,
                JsonRoot.class);
        }
    }
}
