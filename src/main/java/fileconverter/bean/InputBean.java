package fileconverter.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;
import java.io.OutputStream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputBean {
    private InputStream existingFile;
    private OutputStream newFile;
    private String existingFileExtension;
}
