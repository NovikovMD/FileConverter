package fileconverter.writers.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fileconverter.bean.json.JsonUpperClass;
import fileconverter.writers.Writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class GsonWriter implements Writer<JsonUpperClass> {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void write(JsonUpperClass upperClass, OutputStream stream) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(stream));
        gson.toJson(upperClass,bufferedWriter);
        bufferedWriter.close();
    }
}
