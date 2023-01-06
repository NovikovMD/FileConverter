package ru.itdt.fileconverter.bean;

import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.notExists;
import static org.apache.commons.io.FilenameUtils.getExtension;

/**
 * Обработчик входных данных.
 */
@Log4j2
public class InputParamValidator {
    private static final String FIRST_EXTENSION = "xml";
    private static final String SECOND_EXTENSION = "json";

    /**
     * Проверяет входные данные и создает InputBean.
     *
     * @param params массив с двумя элементами:
     *               params[0] - путь к существующему xml\json файлу.
     *               params[1] - путь к новому xml\json файлу.
     * @return InputBean - входные данные для FileConverter.doParse.
     * @throws IllegalArgumentException если переданы некорректные входные данные.
     * @throws IOException              в случае передачи параметром
     *                                  некорректного пути к файлу.
     */
    public static InputParams createBean(final String[] params) throws IllegalArgumentException, IOException {
        if (log.isInfoEnabled()) {
            log.info("Начало создания bean");
        }

        if (params.length != 2) {
            if (params.length<2) {
                throw new IllegalArgumentException("Некорректный ввод данных");
            }

            log.warn("Передано избыточное количество параметров");
        }

        val firstPath = params[0];
        val secondPath = params[1];
        if (firstPath == null){
            throw new IllegalArgumentException("Первый параметр null.");
        }
        if (secondPath == null) {
            throw new IllegalArgumentException("Второй параметр null.");
        }

        if (!(getExtension(firstPath).equals(SECOND_EXTENSION) &&
            getExtension(secondPath).equals(FIRST_EXTENSION)) &&
            !(getExtension(firstPath).equals(FIRST_EXTENSION) &&
                getExtension(secondPath).equals(SECOND_EXTENSION))) {
            throw new IllegalArgumentException("Некорректные расширения файлов.");
        }

        if (notExists(Path.of(firstPath))) {
            throw new FileNotFoundException("Некорректный путь к существующему файлу.");
        }
        if (notExists(Path.of(secondPath).getParent())) {
            throw new FileNotFoundException("Некорректный путь к папке создания нового файла.");
        }

        if (log.isInfoEnabled()) {
            log.info("Валидация входных параметров прошла успешно");
        }

        return InputParams.builder()
            .existingFile(firstPath)
            .newFile(secondPath)
            .build();
    }
}
