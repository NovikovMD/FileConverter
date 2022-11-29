/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */

import file_converter.json_to_xml.JsonToXml;
import file_converter.xml_to_json.XmlToJson;
import logger.Logger;

import java.util.Scanner;

/**
 * Главный класс для запуска сервиса
 */
public class Main {
    /**
     * Запускает конвертирование xml\json файлов в зависимости от входных данных
     *
     * @param args Требуется два элемента:
     *             1 - путь к существующему xml файлу;
     *             2 - путь к новому json файлу.
     */
    public static void main(String[] args) throws Exception {
        Logger.getInstance().info("Начало работы программы");

        if (args == null) {
            Logger.getInstance().error("Некорректный ввод данных. Завершение программы.");
            throw new Exception("Некорректный ввод данных.");
        }

        if (args.length == 1) {
            Logger.getInstance().error("Некорректный ввод данных. Завершение программы.");
            throw new Exception("Некорректный ввод данных.");
        }
        String path;
        String newPath;


        if (args.length == 0) {
            Logger.getInstance().warn("Нет входных данных. Попытка запроса у пользователя.");

            Scanner inp = new Scanner(System.in);
            System.out.print("Введите абсолютный путь к файлу: ");
            path = inp.nextLine();

            System.out.print("Введите абсолютный путь к создаваемому файлу: ");
            newPath = inp.nextLine();
        } else {
            path = args[0];
            newPath = args[1];
        }


        String firstExtension = getExtension(path);
        String secondExtension = getExtension(newPath);

        if (firstExtension.equals("json") && secondExtension.equals("xml")) {
            new JsonToXml().parseJson(path, newPath);
        } else if (firstExtension.equals("xml") && secondExtension.equals("json")) {
            new XmlToJson().parseXml(path, newPath);
        } else {
            Logger.getInstance().error("Некорректный формат входных данных. Завершение программы.");
            throw new Exception("Некорректный формат входных данных.");
        }

        Logger.getInstance().info("Успешное завершение работы программы");
    }

    private static String getExtension(final String newPath) {
        int index = newPath.lastIndexOf(".");
        return index > -1 ? newPath.substring(index + 1) : "";
    }
}