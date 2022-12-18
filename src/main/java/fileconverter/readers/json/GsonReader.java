package fileconverter.readers.json;

import com.google.gson.Gson;
import fileconverter.bean.json.JsonUpper;
import fileconverter.readers.Reader;
import lombok.extern.log4j.Log4j2;

import java.io.*;

/**
 * Создает Json файл используя Gson.
 */
@Log4j2
public class GsonReader implements Reader<JsonUpper> {
    private final Gson gson = new Gson();

    /**
     * Считывает данные из Json файла.
     *
     * @param file путь к существующему файлу.
     * @return класс, содержащий данные из исходного Json файла.
     * @throws IOException при ошибке создания потока входных данных.
     */
    @Override
    public JsonUpper parse(final String file) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Начало работы парсера Gson");
        }

        try (final FileInputStream stream = new FileInputStream(file)) {
            return gson.fromJson(
                new BufferedReader(new InputStreamReader(stream)),
                JsonUpper.class);
        }
    }
}
