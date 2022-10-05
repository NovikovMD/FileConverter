package org.example;

import FileConverter.FileConverter;

public class Main {
    public static void main(String[] args) {
        FileConverter fl = new FileConverter();
        fl.convertToJson("src\\test\\resources\\TestInput.xml", "NewName");
    }
}