import fileconverter.FileConverter;
import org.junit.Before;
import org.junit.Test;

import static fileconverter.bean.BeanCreator.createBean;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BeanCreatorTest {

    FileConverter fileConverter;

    @Before
    public void atStart() {
        fileConverter = new FileConverter();
    }

    @Test
    public void noParameters1() {
        assertEquals("bean is marked non-null but is null",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(null))
                .getMessage());
    }

    @Test
    public void noParameters2() {
        assertEquals("Некорректный ввод данных.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(new String[]{"Some/Path", null})))
                .getMessage());

        assertEquals("Некорректный ввод данных.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(new String[]{null, "Some/Path"})))
                .getMessage());

        assertEquals("Некорректный ввод данных.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(new String[]{null, null})))
                .getMessage());
    }

    @Test
    public void wrongParameters1() {
        assertEquals("Неверный путь к файлу.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/DoesntExist.xml",
                            "src/test/resources/TestMain.json"})))
                .getMessage());
    }

    @Test
    public void wrongParameters2() {
        assertEquals("Неверный путь к файлу.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/DoesntExist.json",
                            "src/test/resources/TestMain.xml"})))
                .getMessage());
    }

    @Test
    public void wrongParameters3() {
        assertEquals("Некорректные расширения файлов.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/IAmJson.json",
                            "src/test/resources/IAmAlsoJson.json"})))
                .getMessage());
    }

    @Test
    public void wrongParameters4() {
        assertEquals("Некорректные расширения файлов.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/IAmXml.xml",
                            "src/test/resources/IAmAlsoXml.xml"})))
                .getMessage());
    }

    @Test
    public void wrongParameters5() {
        assertEquals("Некорректные расширения файлов.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/WrongExtension.mp3",
                            "src/test/resources/TestMain.txt"})))
                .getMessage());
    }
}
