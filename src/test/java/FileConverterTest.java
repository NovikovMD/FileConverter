import fileconverter.FileConverter;

import static fileconverter.bean.BeanCreator.createBean;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileConverterTest {
    FileConverter fileConverter;

    @BeforeEach
    void atStart() {
        fileConverter = new FileConverter();
    }

    @Test
    void correctBehavior1() throws Exception {
        File fl = new File("src/test/resources/TestMain.xml");
        if (fl.exists())
            fl.delete();

        fileConverter.doParse(createBean(
            new String[]{"src/test/resources/TestInput.json",
                "src/test/resources/TestMain.xml"}));

        if (new File("src/test/resources/TestMain.xml").exists())
            assertTrue(true);
        else
            fail("File not created");
    }

    @Test
    void correctBehavior2() throws Exception {
        File fl = new File("src/test/resources/TestMain.json");
        if (fl.exists())
            fl.delete();

        fileConverter.doParse(createBean(
            new String[]{"src/test/resources/TestInput.xml",
                "src/test/resources/TestMain.json"}));

        if (new File("src/test/resources/TestMain.json").exists())
            assertTrue(true);
        else
            fail("File not created");
    }

    @Test
    void error() {
        //TODO придумать ошибочный путь для парсера
    }
}
