package fileconverter.converters;

import lombok.NonNull;

public interface Converter<T, ST> {
    ST convert(@NonNull final T upperClass) throws IllegalArgumentException;
}
