package com.geektime.tdd;

/**
 * IllegalOptionException
 *
 * @author tengxq
 */
public class IllegalOptionException extends RuntimeException {

    private final String value;

    public IllegalOptionException(String value) {
        super(value);
        this.value = value;
    }

    public String getOption() {
        return value;
    }
}
