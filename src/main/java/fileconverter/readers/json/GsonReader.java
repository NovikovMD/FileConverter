package fileconverter.readers.json;

import com.google.gson.Gson;
import fileconverter.bean.json.JsonUpper;
import fileconverter.readers.Reader;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Создает Json файл используя Gson.
 */
@Log4j2
public class GsonReader implements Reader<JsonUpper> {
    private final Gson gson = new Gson();

    /**
     * Считывает данные из Json файла.
     *
     * @param stream источник Json файла.
     * @return класс, содержащий данные из исходного Json файла.
     */
    @Override
    public JsonUpper parse(final InputStream stream) {
        if (log.isDebugEnabled()) {
            log.debug("Начало работы парсера Gson");
        }

        return gson.fromJson(
            new BufferedReader(new InputStreamReader(stream)),
            JsonUpper.class);
    }
}
