package fileconverter.writers.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fileconverter.bean.json.JsonUpper;
import fileconverter.writers.Writer;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Создает Json файл используя Gson.
 */
@Log4j2
public class GsonWriter implements Writer<JsonUpper> {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Создает Json файл.
     *
     * @param data класс, содержащий данные для Json файла.
     * @param stream     приёмник данных для Json файла.
     * @throws IOException если произошла ошибка записи в файл.
     */
    @Override
    public void write(final JsonUpper data, final OutputStream stream) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Начало создания файла Gson");
        }

        val bufferedWriter = new BufferedWriter(new OutputStreamWriter(stream));
        gson.toJson(data, bufferedWriter);
        bufferedWriter.close();

        if (log.isDebugEnabled()) {
            log.debug("Создание файла Json прошло успешно");
        }
    }
}
