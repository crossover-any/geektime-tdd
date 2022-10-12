package com.geektime.tdd;

import java.util.List;
import java.util.function.Function;

/**
 * IntParser
 *
 * @author tengxq
 */
class IntParser implements OptionParser {

    Function<String, Object> valueParser = Integer::parseInt;

    public IntParser() {
    }

    public IntParser(Function<String, Object> valueParser) {
        this.valueParser = valueParser;
    }

    @Override
    public Object parse(List<String> arguments, Option option) {
        int valueIndex = arguments.indexOf(option.value()) + 1;
        return valueParser.apply(arguments.get(valueIndex));
    }

}
