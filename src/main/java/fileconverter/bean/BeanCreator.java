package fileconverter.bean;

import lombok.extern.log4j.Log4j2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.notExists;
import static org.apache.commons.io.FilenameUtils.getExtension;

/**
 * Обработчик входных данных.
 */
@Log4j2
public class BeanCreator {

    /**
     * Проверяет входные данные и создает InputBean.
     *
     * @param params массив с двумя элементами:
     *               params[0] - путь к существующему xml\json файлу.
     *               params[1] - путь к новому xml\json файлу.
     * @return InputBean - входные данные для FileConverter.doParse.
     * @throws IllegalArgumentException если переданы некорректные входные данные.
     * @throws IOException              в случае любой IO ошибки.
     */
    public static InputBean createBean(final String[] params) throws IllegalArgumentException, IOException {
        if (log.isInfoEnabled()) {
            log.info("Начало создания bean");
        }

        if (params.length != 2)
            throw new IllegalArgumentException("Некорректный ввод данных");

        if (params[0] == null ||
            params[1] == null) {
            throw new IllegalArgumentException("Некорректный ввод данных.");
        }

        if (!(getExtension(params[0]).equals("json") &&
            getExtension(params[1]).equals("xml")) &&
            !(getExtension(params[0]).equals("xml") &&
                getExtension(params[1]).equals("json"))) {
            throw new IllegalArgumentException("Некорректные расширения файлов.");
        }

        if (notExists(Path.of(params[0])) || notExists(Path.of(params[1]).getParent()))
            throw new FileNotFoundException("Некорректный путь к файлу.");

        if (log.isInfoEnabled()) {
            log.info("Валидация входных параметров прошла успешно");
        }

        return new InputBean(params[0], params[1]);
    }
}
