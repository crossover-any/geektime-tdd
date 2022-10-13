package com.geektime.tdd;

import java.util.List;
import java.util.function.Function;

/**
 * SingleValueOptionParse
 *
 * @author tengxq
 */
class SingleValueOptionParser<T> implements OptionParser<T> {

    Function<String, T> valueParser;

    T defaultValue;

    public SingleValueOptionParser(T defaultValue, Function<String, T> valueParser) {
        this.valueParser = valueParser;
        this.defaultValue = defaultValue;
    }

    @Override
    public T parse(List<String> arguments, Option option) {
        int valueIndex = arguments.indexOf(option.value()) + 1;
        if (valueIndex == 0) {
            return defaultValue;
        }
        if (valueIndex >= arguments.size() || arguments.get(valueIndex).startsWith("-")) {
            throw new InsufficientArgumentsException(option.value());
        }
        if (valueIndex + 1 < arguments.size() && !arguments.get(valueIndex + 1).startsWith("-")) {
            throw new TooManyArgumentsException(option.value());
        }
        return valueParser.apply(arguments.get(valueIndex));
    }

}
