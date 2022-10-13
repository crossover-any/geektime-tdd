package com.geektime.tdd;

import java.util.List;
import java.util.function.Function;

/**
 * SingleValueOptionParse
 *
 * @author tengxq
 */
class SingleValueOptionParse<T> implements OptionParser {

    Function<String, T> valueParser;

    public SingleValueOptionParse(Function<String, T> valueParser) {
        this.valueParser = valueParser;
    }

    @Override
    public T parse(List<String> arguments, Option option) {
        int valueIndex = arguments.indexOf(option.value()) + 1;
        return valueParser.apply(arguments.get(valueIndex));
    }

}
