package com.geektime.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * SingleValueOptionParserTest
 *
 * @author tengxq
 */
class SingleValueOptionParserTest {

    // sad path
    @Test
    void should_not_accept_extra_argument_for_single_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new SingleValueOptionParser<>(0, Integer::parseInt).parse(asList("-p", "8080", "8081"), option("-p"));
        });
        assertEquals("-p", e.getOption());
    }

    @ParameterizedTest // sad path
    @ValueSource(strings = {"-p -l", "-p"})
    void should_not_accept_extra_arguments_for_single_option(String arguments) {
        InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () -> {
            new SingleValueOptionParser<>(0, Integer::parseInt).parse(asList(arguments.split(" ")), option("-p"));
        });
        assertEquals("-p", e.getOption());
    }

    @Test // default value
    void should_set_default_value_for_single_option() {
        Function<String, Object> whatever = it -> null;
        Object defaultValue = new Object();
        assertEquals(defaultValue, new SingleValueOptionParser<>(defaultValue, whatever).parse(new ArrayList<>(), option("-p")));
    }

    @Test // happy path
    void should_set_parse_value_for_single_option() {
        Object parsedObject = new Object();
        Function<String, Object> parse = it -> parsedObject;
        Object defaultValue = new Object();
        assertEquals(parsedObject, new SingleValueOptionParser<>(defaultValue, parse).parse(asList("-p", "8080"), option("-p")));
    }


    static Option option(String value) {
        return new Option() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return Option.class;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }
}
