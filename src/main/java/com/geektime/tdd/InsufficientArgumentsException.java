package com.geektime.tdd;

/**
 * InsufficientArgumentsException
 *
 * @author tengxq
 */
public class InsufficientArgumentsException extends RuntimeException {

    private final String value;

    public InsufficientArgumentsException(String value) {
        super(value);
        this.value = value;
    }

    public String getOption() {
        return value;
    }
}
