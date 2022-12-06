package fileconverter.bean;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;

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
     * @throws Exception если переданы некорректные входные данные.
     */
    public static InputBean createBean(String[] params) throws Exception {
        log.log(Level.INFO,"Начало создания bean");
        if (params.length != 2)
            throw new Exception("Некорректный ввод данных");

        if (params[0] == null ||
            params[1] == null) {
            throw new Exception("Некорректный ввод данных.");
        }

        if (!(getExtension(params[0]).equals("json") &&
            getExtension(params[1]).equals("xml")) &&
            !(getExtension(params[0]).equals("xml") &&
                getExtension(params[1]).equals("json"))) {
            //log.error("Некорректный формат входных данных. Завершение программы.");
            throw new Exception("Некорректные расширения файлов.");
        }

        log.log(Level.INFO,"Валидация входных параметров прошла успешно");
        return new InputBean(params[0], params[1]);
    }
}
