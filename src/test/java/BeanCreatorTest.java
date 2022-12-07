import fileconverter.FileConverter;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static fileconverter.bean.BeanCreator.createBean;
import static java.nio.file.Files.notExists;
import static org.junit.jupiter.api.Assertions.*;

public class BeanCreatorTest {

    FileConverter fileConverter;

    @BeforeEach
    void atStart() {
        fileConverter = new FileConverter();
    }

    @Test
    void correctBehaviour() throws IOException {
        val bean = createBean(new String[]{"src/test/resources/TestInput.xml",
            "src/test/resources/TestMain.json"});
        if (bean.getExistingFile().readAllBytes().length==0 ||
            notExists(Path.of("src/test/resources/TestMain.json")))
            fail("Некорректное создание bean");
    }

    @Test
    void noParameters1() {
        assertEquals("bean is marked non-null but is null",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(null))
                .getMessage());
    }

    @Test
    void noParameters2() {
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
    void wrongParameters1() {
        assertEquals("Некорректный путь к файлу.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/DoesntExist.xml",
                            "src/test/resources/TestMain.json"})))
                .getMessage());
    }

    @Test
    void wrongParameters2() {
        assertEquals("Некорректный путь к файлу.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/DoesntExist.json",
                            "src/test/resources/TestMain.xml"})))
                .getMessage());
    }

    @Test
    void wrongParameters3() {
        assertEquals("Некорректные расширения файлов.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/IAmJson.json",
                            "src/test/resources/IAmAlsoJson.json"})))
                .getMessage());
    }

    @Test
    void wrongParameters4() {
        assertEquals("Некорректные расширения файлов.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/IAmXml.xml",
                            "src/test/resources/IAmAlsoXml.xml"})))
                .getMessage());
    }

    @Test
    void wrongParameters5() {
        assertEquals("Некорректные расширения файлов.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/WrongExtension.mp3",
                            "src/test/resources/TestMain.txt"})))
                .getMessage());
    }


    @Test
    void wrongPathToDirectory1() {
        assertEquals("Некорректный путь к файлу.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/TestInput.json",
                            "src/NonExistingDirectory/TestMain.xml"})))
                .getMessage());
    }

    @Test
    void wrongPathToDirectory2() {
        assertEquals("Некорректный путь к файлу.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/TestInput.xml",
                            "src/NonExistingDirectory/TestMain.json"})))
                .getMessage());
    }
}
