package fileconverter.readers.json;

import com.google.gson.Gson;
import fileconverter.bean.json.JsonUpperClass;
import fileconverter.readers.Reader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GsonReader implements Reader<JsonUpperClass> {
    private final Gson gson = new Gson();
    @Override
    public JsonUpperClass parse(InputStream stream) {
        return gson.fromJson(
            new BufferedReader(new InputStreamReader(stream)),
            JsonUpperClass.class);
    }
}
