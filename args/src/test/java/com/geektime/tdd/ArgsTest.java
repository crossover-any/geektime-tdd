package com.geektime.tdd;

import com.geektime.tdd.annotation.Option;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ArgsTest
 *
 * @author tengxq
 */
public class ArgsTest {

    // -l -p 8080 -d /usr/logs
    // Happy Path
    // Single Option
    //   bool -l
    //   int -p 8080
    //   str -d /usr/logs
    //   Multi options -l -p 8080 -d /usr/logs
    // Sad Path
    // TODO -bool -l t或者 -l t f
    // TODO -int -p 或者 -p 8080 8081
    // TODO -str -d 或者 -d /usr/logs /usr/vars
    // Default Value
    // TODO -bool false
    // TODO -int 0
    // TODO -str ""

    @Test
    void should_set_true_if_bool_option_flag_present() {
        BoolOption option = Args.parse(BoolOption.class, "-l");
        assertTrue(option.logging());
    }

    @Test
    void should_set_false_if_bool_option_flag_not_present() {
        BoolOption option = Args.parse(BoolOption.class, "-d");
        Assertions.assertFalse(option.logging());
    }

    @Test
    void should_parse_int_as_integer_option_value() {
        IntegerOption option = Args.parse(IntegerOption.class, "-p", "8080");
        assertEquals(8080, option.port());
    }

    @Test
    void should_parse_str_as_string_option_value() {
        StringOption option = Args.parse(StringOption.class, "-d", "/usr/logs");
        assertEquals("/usr/logs", option.directory());
    }

    @Test
    void should_parse_multi_as_multi_option_value() {
        MultiOption option = Args.parse(MultiOption.class, "-l", "-p", "8080", "-d", "/usr/log");
        assertTrue(option.logging());
        assertEquals(8080, option.port());
        assertEquals("/usr/log", option.directory());
    }

    static record BoolOption(@Option("-l") Boolean logging) {
    }

    static record IntegerOption(@Option("-p") Integer port) {
    }

    static record StringOption(@Option("-d") String directory) {
    }

    static record MultiOption(@Option("-l") Boolean logging,
                              @Option("-p") Integer port,
                              @Option("-d") String directory) {
    }
}
