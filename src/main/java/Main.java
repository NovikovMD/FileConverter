import fileconverter.FileConverter;

import static fileconverter.bean.BeanCreator.createBean;

/**
 * Главный класс для запуска сервиса
 */
public class Main {
    public static void main(String[] args) throws Exception {
        new FileConverter().doParse(createBean(args));
    }
}