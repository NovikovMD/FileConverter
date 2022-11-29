import file_converter.FileConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

public class FileConverterTest {
    FileConverter fileConverter;

    @Before
    public void atStart(){
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
        String[] str = null;

        try {
            fileConverter.doParse(Arrays.stream(str).toList());
            Assert.fail("Обработы неверные параметры");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void noParameters2() {
        String[] str = {"Some\\Path"};

        try {
            fileConverter.doParse(Arrays.stream(str).toList());
            Assert.fail("Обработы неверные параметры");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void wrongParameters1() {
        String[] str = {"src/test/resources/DoesntExist.xml", "src/test/resources/TestMain.json"};

        try {
            fileConverter.doParse(Arrays.stream(str).toList());
            Assert.fail("Обработы неверные параметры");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void wrongParameters2() {
        String[] str = {"src/test/resources/DoesntExist.json", "src/test/resources/TestMain.xml"};

        try {
            fileConverter.doParse(Arrays.stream(str).toList());
            Assert.fail("Обработы неверные параметры");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void wrongParameters3() {
        String[] str = {"src/test/resources/DoesntExist.json", "src/test/resources/TestMain.json"};

        try {
            fileConverter.doParse(Arrays.stream(str).toList());
            Assert.fail("Обработы неверные параметры");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void wrongParameters4() {
        String[] str = {"src/test/resources/DoesntExist.xml", "src/test/resources/TestMain.xml"};

        try {
            fileConverter.doParse(Arrays.stream(str).toList());
            Assert.fail("Обработы неверные параметры");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void wrongParameters5() {
        String[] str = {"src/test/resources/DoesntExist.mp3", "src/test/resources/TestMain.txt"};

        try {
            fileConverter.doParse(Arrays.stream(str).toList());
            Assert.fail("Обработы неверные параметры");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void wrongPathToDirectory1() {
        String[] str = {"src/test/resources/TestInput.json", "src/NonExistingDirectory/TestMain.xml"};

        try {
            fileConverter.doParse(Arrays.stream(str).toList());
            Assert.fail("Обработы неверные параметры");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void wrongPathToDirectory2() {
        String[] str = {"src/test/resources/TestInput.xml", "src/NonExistingDirectory/TestMain.json"};

        try {
            fileConverter.doParse(Arrays.stream(str).toList());
            Assert.fail("Обработы неверные параметры");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }
}
