package com.gabrielittner.threetenbp;

import com.google.devtools.common.options.Converter;
import com.google.devtools.common.options.OptionsParsingException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathConverter implements Converter<Path> {
    public PathConverter() {}

    @Override
    public Path convert(String input) throws OptionsParsingException {
        return input.isEmpty() ? null : Paths.get(input);
    }

    @Override
    public String getTypeDescription() {
        return "File path";
    }
}
