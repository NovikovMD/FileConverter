import fileconverter.FileConverter;

import static fileconverter.bean.BeanCreator.createBean;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class FileConverterTest {
    FileConverter fileConverter;

    @Before
    public void atStart() {
        fileConverter = new FileConverter();
    }

    @Test
    public void correctBehavior1() throws Exception {
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
    public void correctBehavior2() throws Exception {
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
    public void wrongPathToDirectory1() {
        assertEquals("src\\NonExistingDirectory\\TestMain.xml (Системе не удается найти указанный путь)",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/TestInput.json",
                            "src/NonExistingDirectory/TestMain.xml"})))
                .getMessage());
    }

    @Test
    public void wrongPathToDirectory2() {
        assertEquals("src\\NonExistingDirectory\\TestMain.json (Системе не удается найти указанный путь)",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/TestInput.xml",
                            "src/NonExistingDirectory/TestMain.json"})))
                .getMessage());
    }
}
