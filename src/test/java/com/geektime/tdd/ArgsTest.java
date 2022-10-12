package com.geektime.tdd;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ArgsTest
 *
 * @author tengxq
 */
class ArgsTest {

    // -l -p 8080 -d /usr/logs
    // Happy Path
    // Single Option
    // bool -l
    // int -p 8080
    // str -d /usr/logs
    // Multi options -l -p 8080 -d /usr/logs
    // Sad Path
    // TODO -bool -l t或者 -l t f
    // TODO -int -p 或者 -p 8080 8081
    // TODO -str -d 或者 -d /usr/logs /usr/vars
    // Default Value
    // TODO -bool false
    // TODO -int 0
    // TODO -str ""

    @Disabled("编译")
    @Test
    void should_compile_pass() {
        Options option = Args.parse(Options.class, "-l", "-p", "8080", "-d", "usr/logs");
        assert option != null;
        assertTrue(option.logging());
    }

    // bool -l
    @Test
    void should_parse_bool_as_option() {
        BoolOption boolOption = Args.parse(BoolOption.class, "-l");
        assert boolOption != null;
        assertTrue(boolOption.logging());
    }

    static record BoolOption(@Option("-l") Boolean logging) {
    }

    // int -p 8080
    @Test
    void should_parse_int_as_option() {
        IntOption intOption = Args.parse(IntOption.class, "-p", "8080");
        assert intOption != null;
        assertEquals(8080, intOption.port());
    }

    static record IntOption(@Option("-p") Integer port) {
    }

    // str -d /usr/logs
    @Test
    void should_parse_str_as_option() {
        StrOption strOption = Args.parse(StrOption.class, "-d", "/usr/logs");
        assert strOption != null;
        assertEquals("/usr/logs", strOption.director());
    }

    static record StrOption(@Option("-d") String director) {
    }

    // Multi options -l -p 8080 -d /usr/logs
    @Test
    void should_parse_multi_as_option() {
        MultiOptions multiOptions = Args.parse(MultiOptions.class, "-l", "-p", "8080", "-d", "/usr/logs");
        assert multiOptions != null;
        assertTrue(multiOptions.logging());
        assertEquals(8080, multiOptions.port());
        assertEquals("/usr/logs", multiOptions.director());
    }

    static record MultiOptions(@Option("-l") Boolean logging, @Option("-p") Integer port,
                               @Option("-d") String director) {
    }


    static record Options(@Option("-l") boolean logging, @Option("-p") int port, @Option("-d") String director) {
    }
}
