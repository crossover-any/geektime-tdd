package com.geektime.tdd;

import com.geektime.tdd.exception.IllegalValueException;
import com.geektime.tdd.exception.InsufficientArgumentsException;
import com.geektime.tdd.exception.TooManyArgumentsException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * OptionParsers
 *
 * @author tengxq
 */
public class OptionParsers {

    private OptionParsers() {
    }

    public static OptionParser<Boolean> bool() {
        return ((arguments, option) -> getValues(arguments, option, 0).isPresent());
    }

    public static <T> OptionParser<T> unary(T defaultValue, Function<String, T> valueParser) {
        return (arguments, option) -> getValues(arguments, option, 1).map(it -> parseValue(option, it.get(0), valueParser)).orElse(defaultValue);
    }

    public static <T> OptionParser<T[]> list(IntFunction<T[]> generator, Function<String, T> valueParser) {
        return ((arguments, option) -> getValues(arguments, option)
                .map(it -> it.stream().map(value -> parseValue(option, value, valueParser))
                        .toArray(generator)).orElse(generator.apply(0)));
    }

    private static Optional<List<String>> getValues(List<String> arguments, Option option, int expectedSize) {
        int valueIndex = arguments.indexOf(option.value());
        if (valueIndex == -1) {
            return Optional.empty();
        }
        List<String> values = getValues(arguments, valueIndex);
        if (values.size() < expectedSize) {
            throw new InsufficientArgumentsException(option.value());
        }
        if (values.size() > expectedSize) {
            throw new TooManyArgumentsException(option.value());
        }
        return Optional.of(values);
    }

    private static Optional<List<String>> getValues(List<String> arguments, Option option) {
        int valueIndex = arguments.indexOf(option.value());
        if (valueIndex == -1) {
            return Optional.empty();
        }
        List<String> values = getValues(arguments, valueIndex);
        return Optional.of(values);
    }

    private static <T> T parseValue(Option option, String value, Function<String, T> valueParser) {
        try {
            return valueParser.apply(value);
        } catch (Exception e) {
            throw new IllegalValueException(option.value(), value);
        }
    }

    private static List<String> getValues(List<String> arguments, int valueIndex) {
        int nextOptionIndex = IntStream.range(valueIndex + 1, arguments.size())
                .filter(it -> arguments.get(it).startsWith("-"))
                .findFirst()
                .orElse(arguments.size());
        return arguments.subList(valueIndex + 1, nextOptionIndex);
    }

}
