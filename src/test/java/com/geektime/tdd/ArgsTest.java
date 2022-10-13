package com.geektime.tdd;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    //  -bool -l t或者 -l t f
    //  -int -p 或者 -p 8080 8081
    //  -str -d 或者 -d /usr/logs /usr/vars
    // Default Value
    //  -bool false
    //  -int 0
    //  -str ""

    @Disabled("编译")
    @Test
    void should_compile_pass() {
        Options option = Args.parse(Options.class, "-l", "-p", "8080", "-d", "usr/logs");
        assert option != null;
        assertTrue(option.logging());
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

    @Test
    void should_throw_illegal_option_exception_if_option_annotation_not_present() {
        IllegalOptionException e = assertThrows(IllegalOptionException.class, () -> Args.parse(OptionsWithoutAnnotation.class, "-l", "-p", "8080", "-d", "/usr/logs"));
        assertEquals("logging", e.getOption());
    }

    static record MultiOptions(@Option("-l") Boolean logging, @Option("-p") Integer port,
                               @Option("-d") String director) {
    }

    static record OptionsWithoutAnnotation(Boolean logging, @Option("-p") Integer port,
                               @Option("-d") String director) {
    }


    static record Options(@Option("-l") boolean logging, @Option("-p") int port, @Option("-d") String director) {
    }
}
