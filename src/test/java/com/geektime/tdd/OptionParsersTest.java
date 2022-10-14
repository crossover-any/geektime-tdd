package com.geektime.tdd;

import com.geektime.tdd.exception.InsufficientArgumentsException;
import com.geektime.tdd.exception.TooManyArgumentsException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * OptionParsersTest
 *
 * @author tengxq
 */
class OptionParsersTest {


    @Nested
    class UnaryOptionParser {
        // sad path
        @Test
        void should_not_accept_extra_argument_for_single_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.unary(0, Integer::parseInt).parse(asList("-p", "8080", "8081"), option("-p"));
            });
            assertEquals("-p", e.getOption());
        }

        @ParameterizedTest // sad path
        @ValueSource(strings = {"-p -l", "-p"})
        void should_not_accept_extra_arguments_for_single_option(String arguments) {
            InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () -> {
                OptionParsers.unary(0, Integer::parseInt).parse(asList(arguments.split(" ")), option("-p"));
            });
            assertEquals("-p", e.getOption());
        }

        @Test
            // default value
        void should_set_default_value_for_single_option() {
            Function<String, Object> whatever = it -> null;
            Object defaultValue = new Object();
            assertEquals(defaultValue, OptionParsers.unary(defaultValue, whatever).parse(new ArrayList<>(), option("-p")));
        }

        @Test
            // happy path
        void should_set_parse_value_for_single_option() {
            Object parsedObject = new Object();
            Function<String, Object> parse = it -> parsedObject;
            Object defaultValue = new Object();
            assertEquals(parsedObject, OptionParsers.unary(defaultValue, parse).parse(asList("-p", "8080"), option("-p")));
        }
    }


    @Nested
    class BooleanParserTest {

        // BooleanOptionParserTest:
        // sad path:
        // -bool -l t / -l t f
        // default:
        // - bool : false

        // Sad Path
        @Test
        void should_not_accept_extra_argument_for_boolean_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.bool().parse(asList("-l", "t"), option("-l"));
            });
            assertEquals("-l", e.getOption());
        }

        // Sad Path
        @Test
        void should_not_accept_extra_arguments_for_boolean_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.bool().parse(asList("-l", "t", "f"), option("-l"));
            });
            assertEquals("-l", e.getOption());
        }

        // Sad Path
        @Test
        void should_set_default_value_to_false_if_boolean_option_not_present() {
            Boolean value = OptionParsers.bool().parse(asList(), option("-l"));
            assertFalse(value);
        }

        // Happy Path
        @Test
        void should_set_value_to_true_if_boolean_option_present() {
            Boolean value = OptionParsers.bool().parse(asList("-l"), option("-l"));
            assertTrue(value);
        }

    }

    @Nested
    class ListOptionParser {
        // TODO -g "this" "is" {"this", "is"}
        @Test
        void should_parse_list_value() {
            String[] value = OptionParsers.list(String[]::new, String::valueOf).parse(asList("-g", "this", "is"), option("-g"));
            assertArrayEquals(new String[]{"this", "is"}, value);
        }
        // TODO default value []
        // TODO -d throw a exception


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
