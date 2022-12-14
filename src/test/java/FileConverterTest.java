import fileconverter.FileConverter;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static fileconverter.bean.BeanCreator.createBean;
import static org.junit.jupiter.api.Assertions.*;

public class FileConverterTest {
    private FileConverter fileConverter;

    @BeforeEach
    void atStart() {
        fileConverter = new FileConverter();
    }

    @Test
    void correctBehavior1() throws Exception {
        val fl = new File("src/test/resources/TestMain.xml");
        if (fl.exists())
            fl.delete();

        fileConverter.doParse(createBean(
            new String[]{"src/test/resources/TestInput.json",
                "src/test/resources/TestMain.xml"}));

        assertTrue(new File("src/test/resources/TestMain.xml").exists());
    }

    @Test
    void correctBehavior2() throws Exception {
        val fl = new File("src/test/resources/TestMain.json");
        if (fl.exists())
            fl.delete();

        fileConverter.doParse(createBean(
            new String[]{"src/test/resources/TestInput.xml",
                "src/test/resources/TestMain.json"}));

        assertTrue(new File("src/test/resources/TestMain.json").exists());
    }

    @Test
    void closedStream1() throws IOException {
        val bean = createBean(
            new String[]{"src/test/resources/TestInput.xml",
                "src/test/resources/TestMain.json"});
        bean.getNewFile().close();

        assertEquals("Stream Closed",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(bean))
                .getMessage());
    }
}
