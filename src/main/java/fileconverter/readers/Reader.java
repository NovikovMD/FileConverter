package fileconverter.readers;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public interface Reader<T> {
    T parse(final InputStream stream)
        throws ParserConfigurationException, SAXException, IOException;
}
