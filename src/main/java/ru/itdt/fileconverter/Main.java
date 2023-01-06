package ru.itdt.fileconverter;

import static ru.itdt.fileconverter.bean.InputParamValidator.createBean;

/**
 * Главный класс для запуска сервиса
 */
public class Main {
    public static void main(String[] args) throws Exception {
        new FileConverter().doParse(createBean(args));
    }
}