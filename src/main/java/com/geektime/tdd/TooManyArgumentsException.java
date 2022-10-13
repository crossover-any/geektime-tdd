package com.geektime.tdd;

/**
 * TooManyArgumentsException
 *
 * @author tengxq
 */
public class TooManyArgumentsException extends RuntimeException {

    private final String value;

    public TooManyArgumentsException(String value) {
        super(value);
        this.value = value;
    }

    public String getOption() {
        return value;
    }
}
