import file_converter.FileConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

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

        String[] str = {"src/test/resources/TestInput.json", "src/test/resources/TestMain.xml"};
        fileConverter.doParse(Arrays.stream(str).toList());

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

        String[] str = {"src/test/resources/TestInput.xml", "src/test/resources/TestMain.json"};
        fileConverter.doParse(Arrays.stream(str).toList());

        fl = new File("src/test/resources/TestMain.json");
        if (fl.exists())
            Assert.assertTrue(true);
        else
            Assert.fail("File not created");
    }

    @Test
    public void noParameters1() {
        List<String> params = null;

        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(params));

        Assert.assertEquals("Некорректный ввод данных.", exception.getMessage());
    }

    @Test
    public void noParameters2() {
        String[] str = {"Some\\Path"};

        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(Arrays.stream(str).toList()));

        Assert.assertEquals("Некорректный ввод данных.", exception.getMessage());
    }

    @Test
    public void wrongParameters1() {
        String[] str = {"src/test/resources/DoesntExist.xml", "src/test/resources/TestMain.json"};

        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(Arrays.stream(str).toList()));

        Assert.assertEquals("Неверный путь к XML файлу.", exception.getMessage());
    }

    @Test
    public void wrongParameters2() {
        String[] str = {"src/test/resources/DoesntExist.json", "src/test/resources/TestMain.xml"};

        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(Arrays.stream(str).toList()));

        Assert.assertEquals("Неверный путь к JSON файлу.", exception.getMessage());
    }

    @Test
    public void wrongParameters3() {
        String[] str = {"src/test/resources/IAmJson.json", "src/test/resources/IAmAlsoJson.json"};

        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(Arrays.stream(str).toList()));

        Assert.assertEquals("Некорректный формат входных данных.",exception.getMessage());
    }

    @Test
    public void wrongParameters4() {
        String[] str = {"src/test/resources/IAmXml.xml", "src/test/resources/IAmAlsoXml.xml"};

        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(Arrays.stream(str).toList()));

        Assert.assertEquals("Некорректный формат входных данных.", exception.getMessage());
    }

    @Test
    public void wrongParameters5() {
        String[] str = {"src/test/resources/WrongExtension.mp3", "src/test/resources/TestMain.txt"};

        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(Arrays.stream(str).toList()));

        Assert.assertEquals("Некорректный формат входных данных.",exception.getMessage());
    }

    @Test
    public void wrongPathToDirectory1() {
        String[] str = {"src/test/resources/TestInput.json", "src/NonExistingDirectory/TestMain.xml"};

        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(Arrays.stream(str).toList()));

        Assert.assertEquals("Введен неверный путь к файлу XML.",exception.getMessage());
    }

    @Test
    public void wrongPathToDirectory2() {
        String[] str = {"src/test/resources/TestInput.xml", "src/NonExistingDirectory/TestMain.json"};

        Exception exception = assertThrows(Exception.class,
            () -> fileConverter.doParse(Arrays.stream(str).toList()));

        Assert.assertEquals("Не удалось создать JSON файл.", exception.getMessage());
    }
}
