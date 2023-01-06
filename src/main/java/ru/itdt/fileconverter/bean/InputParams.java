package ru.itdt.fileconverter.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputParams {
    private String existingFile;
    private String newFile;

    private InputParams(Builder builder) {
        existingFile = builder.existingFile;
        newFile = builder.newFile;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private String existingFile;
        private String newFile;

        public Builder existingFile(String existingFile) {
            this.existingFile = existingFile;
            return this;
        }

        public Builder newFile(String newFile) {
            this.newFile = newFile;
            return this;
        }

        public InputParams build(){
            return new InputParams(this);
        }
    }
}
