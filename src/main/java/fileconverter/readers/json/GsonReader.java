package fileconverter.readers.json;

import com.google.gson.Gson;
import fileconverter.bean.json.JsonUpperClass;
import fileconverter.readers.Reader;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Создает Json файл используя Gson.
 */
@Log4j2
public class GsonReader implements Reader<JsonUpperClass> {
    private final Gson gson = new Gson();

    /**
     * Считывает данные из Json файла.
     *
     * @param stream источник Json файла.
     * @return класс, содержащий данные из исходного Json файла.
     */
    @Override
    public JsonUpperClass parse(final InputStream stream) {
        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Начало работы парсера Gson");

        return gson.fromJson(
            new BufferedReader(new InputStreamReader(stream)),
            JsonUpperClass.class);
    }
}
