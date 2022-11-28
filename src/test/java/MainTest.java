import logger.Logger;
import org.apache.log4j.Level;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class MainTest {
    @Before
    public void setProperties() {
        Logger.getInstance().setLevel(Level.ALL);
    }

    @Test
    public void correctBehavior1() {
        File fl = new File("src/test/resources/TestMain.xml");
        if (fl.exists())
            fl.delete();

        String[] str = {"src/test/resources/TestInput.json", "src/test/resources/TestMain.xml"};
        Main.main(str);

        fl = new File("src/test/resources/TestMain.xml");

        if (fl.exists())
            Assert.assertTrue(true);
        else
            Assert.fail("File not created");
    }

    @Test
    public void correctBehavior2() {
        File fl = new File("src/test/resources/TestMain.json");
        if (fl.exists())
            fl.delete();

        String[] str = {"src/test/resources/TestInput.xml", "src/test/resources/TestMain.json"};
        Main.main(str);

        fl = new File("src/test/resources/TestMain.json");
        if (fl.exists())
            Assert.assertTrue(true);
        else
            Assert.fail("File not created");
    }

    @Test
    public void noParameters1() throws IOException {
        String[] str = null;

        Main.main(str);//no exceptions
    }

    @Test
    public void noParameters2() {
        String[] str = {"Some\\Path"};

        Main.main(str);//no exceptions
    }

    @Test
    public void wrongParameters1() {
        String[] str = {"src/test/resources/DoesntExist.xml", "src/test/resources/TestMain.json"};

        Main.main(str);//no exceptions
    }

    @Test
    public void wrongParameters2() {
        String[] str = {"src/test/resources/DoesntExist.json", "src/test/resources/TestMain.xml"};

        Main.main(str);//no exceptions
    }

    @Test
    public void wrongParameters3() {
        String[] str = {"src/test/resources/DoesntExist.json", "src/test/resources/TestMain.json"};

        Main.main(str);//no exceptions
    }

    @Test
    public void wrongParameters4() {
        String[] str = {"src/test/resources/DoesntExist.xml", "src/test/resources/TestMain.xml"};

        Main.main(str);//no exceptions
    }

    @Test
    public void wrongParameters5() {
        String[] str = {"src/test/resources/DoesntExist.mp3", "src/test/resources/TestMain.txt"};

        Main.main(str);//no exceptions
    }

    @Test
    public void wrongPathToDirectory1() {
        String[] str = {"src/test/resources/TestInput.json", "src/NonExistingDirectory/TestMain.xml"};

        Main.main(str);//no exceptions
    }

    @Test
    public void wrongPathToDirectory2() {
        String[] str = {"src/test/resources/TestInput.xml", "src/NonExistingDirectory/TestMain.json"};

        Main.main(str);//no exceptions
    }
}
