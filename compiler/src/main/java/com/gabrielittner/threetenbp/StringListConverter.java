package com.gabrielittner.threetenbp;

import com.google.devtools.common.options.Converter;
import com.google.devtools.common.options.OptionsParsingException;
import java.util.Arrays;
import java.util.List;

public final class StringListConverter implements Converter<List<String>> {
    public StringListConverter() {}

    @Override
    public List<String> convert(String input) throws OptionsParsingException {
        return Arrays.asList(input.split(","));
    }

    @Override
    public String getTypeDescription() {
        return "File path";
    }
}
