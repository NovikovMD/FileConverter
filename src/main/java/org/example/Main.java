package org.example;

import FileConverter.FileConverter;

public class Main {
    public static void main(String[] args) throws Exception {
        FileConverter fl = new FileConverter();
        if (args.length != 0)
            if (args[0].contains(".json"))
                fl.convertToXML(args[0], args[1]);
            else if (args[0].contains(".xml"))
                fl.convertToJson(args[0], args[1]);
            else
                throw new Exception("Wrong input");
        else{
            fl.convertToJson("src/test/resources/TestInput.xml", "Somebody.json");
            fl.doSum();

        }
    }
}