package fileconverter.writers;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStream;

public interface Writer<T> {
    void write(final T upperClass, final OutputStream stream) throws IOException, XMLStreamException;
}
