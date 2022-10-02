import FileConverter.XML_to_JSON.XML_to_JSON;
import org.junit.Assert;
import org.junit.Test;

public class Test_XMLtoJSON {

    @Test
    public void parseDocument(){
        Assert.assertNotNull(XML_to_JSON.parseXML("TestInput.xml"));
    }

}
