import fileconverter.FileConverter;
import fileconverter.bean.InputBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertThrows;

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

        fileConverter.doParse(new InputBean("src/test/resources/TestInput.json",
            "src/test/resources/TestMain.xml"));

        fl = new File("src/test/resources/TestMain.xml");

        if (fl.exists())
            Assert.assertTrue(true);
        else
            Assert.fail("File not created");
    }

    @Test
    public void correctBehavior2() throws Exception {
        File fl = new File("src/test/resources/TestMain.json");
        if (fl.exists())
            fl.delete();

        fileConverter.doParse(new InputBean("src/test/resources/TestInput.xml",
            "src/test/resources/TestMain.json"));

        fl = new File("src/test/resources/TestMain.json");
        if (fl.exists())
            Assert.assertTrue(true);
        else
            Assert.fail("File not created");
    }

    @Test
    public void noParameters1() {
        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(null));

        Assert.assertEquals("bean is marked non-null but is null", exception.getMessage());
    }

    @Test
    public void noParameters2() {
        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(new InputBean("Some/Path",null)));

        Assert.assertEquals("Некорректный ввод данных.", exception.getMessage());

        exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(new InputBean(null,"Some/Path")));

        Assert.assertEquals("Некорректный ввод данных.", exception.getMessage());

    }

    @Test
    public void wrongParameters1() {
        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(new InputBean("src/test/resources/DoesntExist.xml",
                "src/test/resources/TestMain.json")));

        Assert.assertEquals("Неверный путь к файлу.", exception.getMessage());
    }

    @Test
    public void wrongParameters2() {
        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(new InputBean("src/test/resources/DoesntExist.json",
                "src/test/resources/TestMain.xml")));

        Assert.assertEquals("Неверный путь к файлу.", exception.getMessage());
    }

    @Test
    public void wrongParameters3() {
        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(new InputBean("src/test/resources/IAmJson.json",
                "src/test/resources/IAmAlsoJson.json")));

        Assert.assertEquals("Некорректный формат входных данных.",exception.getMessage());
    }

    @Test
    public void wrongParameters4() {
        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(new InputBean("src/test/resources/IAmXml.xml",
                "src/test/resources/IAmAlsoXml.xml")));

        Assert.assertEquals("Некорректный формат входных данных.", exception.getMessage());
    }

    @Test
    public void wrongParameters5() {
        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(new InputBean("src/test/resources/WrongExtension.mp3",
                "src/test/resources/TestMain.txt")));

        Assert.assertEquals("Некорректный формат входных данных.",exception.getMessage());
    }

    @Test
    public void wrongPathToDirectory1() {
        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(new InputBean("src/test/resources/TestInput.json",
                "src/NonExistingDirectory/TestMain.xml")));

        Assert.assertEquals("src\\NonExistingDirectory\\TestMain.xml (Системе не удается найти указанный путь)",
            exception.getMessage());
    }

    @Test
    public void wrongPathToDirectory2() {
        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(new InputBean("src/test/resources/TestInput.xml",
                "src/NonExistingDirectory/TestMain.json")));

        Assert.assertEquals("src\\NonExistingDirectory\\TestMain.json (Системе не удается найти указанный путь)",
            exception.getMessage());
    }
}
