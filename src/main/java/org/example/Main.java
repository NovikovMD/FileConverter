/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package org.example;

import FileConverter.FileConverter;

import java.util.Scanner;

/**
 * Starter class for FileConverter
 * @author Novikov Matthew
 */
public class Main {
    /**
     * Starts converting for xml or json file depending on the input data
     * @param args Required 2 parameters: 1 - path to existing file; 2 - path to new file
     */
    public static void main(String[] args) {
        String path;
        String newPath;

        if (args.length == 0) {
            Scanner inp = new Scanner(System.in);
            System.out.print("Input full path to file: ");
            path = inp.nextLine();

            System.out.print("Input full path to new file: ");
            newPath = inp.nextLine();
        } else {
            path = args[0];
            newPath = args[1];
        }

        FileConverter fl = new FileConverter();
        if (args[0].contains(".json")) {
            fl.convertToXML(path, newPath);
        }
        else if (args[0].contains(".xml")) {
            fl.convertToJson(path, newPath);
        }
        else {
            System.out.print("Wrong input");
        }
    }
}