package ru.itdt.fileconverter;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.itdt.fileconverter.bean.InputParamValidator.createBean;

public class InputParamValidatorTest {

    FileConverter fileConverter;

    @BeforeEach
    void atStart() {
        fileConverter = new FileConverter();
    }

    @Test
    void correctBehaviour() throws IOException {
        val bean = createBean(new String[]{"src/test/resources/TestInput.xml",
            "src/test/resources/TestMain.json"});
        if (bean.getExistingFile() == null ||
            bean.getNewFile() == null)
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
        assertEquals("Второй параметр null.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(new String[]{"Some/Path", null})))
                .getMessage());

        assertEquals("Первый параметр null.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(new String[]{null, "Some/Path"})))
                .getMessage());
    }

    @Test
    void wrongParameters1() {
        assertEquals("Некорректный путь к существующему файлу.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/DoesntExist.xml",
                            "src/test/resources/TestMain.json"})))
                .getMessage());
    }

    @Test
    void wrongParameters2() {
        assertEquals("Некорректный путь к существующему файлу.",
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
        assertEquals("Некорректный путь к папке создания нового файла.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/TestInput.json",
                            "src/NonExistingDirectory/TestMain.xml"})))
                .getMessage());
    }

    @Test
    void wrongPathToDirectory2() {
        assertEquals("Некорректный путь к папке создания нового файла.",
            assertThrows(Exception.class,
                () -> fileConverter.doParse(
                    createBean(
                        new String[]{"src/test/resources/TestInput.xml",
                            "src/NonExistingDirectory/TestMain.json"})))
                .getMessage());
    }
}
