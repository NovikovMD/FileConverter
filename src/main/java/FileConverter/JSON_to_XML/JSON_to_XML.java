package FileConverter.JSON_to_XML;

import FileConverter.Classes.JSON.JSON;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.File;
import java.io.IOException;

public class JSON_to_XML {

    public static JSON parseXML(String path) throws IOException {
        File fl = new File(path);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(fl);

        //point to
        parser.nextToken();
        parser.nextToken();
        JSON jsonOutput = new JSON();


        if (parser.nextToken() == JsonToken.START_ARRAY) {
            //loop until token equal to "]"
            while (parser.nextToken() != JsonToken.END_ARRAY) {

                //checker if we're looking at "{" / "}"
                if (parser.getCurrentName()==null)
                    continue;
                String key = parser.getCurrentName();

                if(key.equals("name"))
                {
                    jsonOutput.addGame("place_holder",-1,"place_holder");
                    parser.nextToken();
                    jsonOutput.getLastGame().setName(parser.getText());
                }
                else if (key.equals("year")){
                    parser.nextToken();
                    jsonOutput.getLastGame().setYear(Integer.parseInt(parser.getText()));
                }
                else if (key.equals("gamePublisher")){
                    parser.nextToken();
                    jsonOutput.getLastGame().setGamePublisher(parser.getText());
                }
                else if (key.equals("platforms")) {
                    parser.nextToken();
                    while (parser.nextToken() != JsonToken.END_ARRAY){
                        jsonOutput.getLastGame().addPlatform(parser.getText());
                    }
                }
                else if (key.equals("developer_studios")) {
                    parser.nextToken();
                    parser.nextToken();

                    //loop until end of devStudios
                    while (parser.nextToken() != JsonToken.END_ARRAY){

                        //checker if we're looking at "{" / "}"
                        if (parser.getCurrentName()==null)
                            continue;
                        if(parser.getCurrentName().equals("name")) {
                            parser.nextToken();
                            jsonOutput.getLastGame().addDevStudio(parser.getText(),-1,"place_holder");
                        }
                        else if(parser.getCurrentName().equals("yearOfFoundation")) {
                            parser.nextToken();
                            jsonOutput.getLastGame().getLastDevStudio().setYearOfFoundation(Integer.parseInt(parser.getText()));
                        }
                        else if(parser.getCurrentName().equals("url")) {
                            parser.nextToken();
                            jsonOutput.getLastGame().getLastDevStudio().setURL(parser.getText());
                        }
                    }
                }

            }
        }
        else
            return null;


        //Getting the token name


        return jsonOutput;
    }

}
