package com.geektime.tdd;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * BooleanParserTest
 *
 * @author tengxq
 */
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
            new BooleanParser().parse(asList("-l", "t"), option("-l"));
        });
        assertEquals("-l", e.getOption());
    }

    // Sad Path
    @Test
    void should_not_accept_extra_arguments_for_boolean_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new BooleanParser().parse(asList("-l", "t", "f"), option("-l"));
        });
        assertEquals("-l", e.getOption());
    }

    // Sad Path
    @Test
    void should_set_default_value_to_false_if_boolean_option_not_present() {
        Boolean value = new BooleanParser().parse(asList(), option("-l"));
        assertFalse(value);
    }

    // Happy Path
    @Test
    void should_set_value_to_true_if_boolean_option_present() {
        Boolean value = new BooleanParser().parse(asList("-l"), option("-l"));
        assertTrue(value);
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
