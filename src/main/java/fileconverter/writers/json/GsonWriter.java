package fileconverter.writers.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fileconverter.bean.json.JsonUpperClass;
import fileconverter.writers.Writer;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.logging.log4j.Level;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Создает Json файл используя Gson.
 */
@Log4j2
public class GsonWriter implements Writer<JsonUpperClass> {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Создает Json файл.
     *
     * @param upperClass класс, содержащий данные для Json файла.
     * @param stream     приёмник данных для Json файла.
     * @throws IOException если произошла ошибка записи в файл.
     */
    @Override
    public void write(final JsonUpperClass upperClass, final OutputStream stream) throws IOException {
        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Начало создания файла Gson");

        val bufferedWriter = new BufferedWriter(new OutputStreamWriter(stream));
        gson.toJson(upperClass, bufferedWriter);
        bufferedWriter.close();

        if (log.isEnabled(Level.DEBUG))
            log.log(Level.DEBUG, "Создание файла Json прошло успешно");
    }
}
