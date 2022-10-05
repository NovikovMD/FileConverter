package FileConverter;

import FileConverter.XML_to_JSON.XML_to_JSON;

public class FileConverter {
    public void convertToJson(String pathToXML, String nameOfFile)  {
        try {
            XML_to_JSON.parseXML(pathToXML);
            XML_to_JSON.createJSON(XML_to_JSON.convert(), getParentName(pathToXML)+nameOfFile+".json");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getParentName(String curDir){
        //create output string
        StringBuilder parentName = new StringBuilder(curDir);

        //init index
        int i=parentName.length()-1;

        while (i>=0){
            //delete name of current directory
            if (parentName.charAt(i) != '\\') {
                parentName.deleteCharAt(i);
                i--;
            }
            else {
                //exit "while"
                i = -1;
            }
        }

        return parentName.toString();
    }
}
