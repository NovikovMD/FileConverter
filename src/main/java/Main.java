/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */

import fileconverter.FileConverter;
import fileconverter.bean.InputBean;

/**
 * Главный класс для запуска сервиса
 */
public class Main {
    public static void main(String[] args) throws Exception {
        new FileConverter().doParse(new InputBean(args[0], args[1]));
    }
}