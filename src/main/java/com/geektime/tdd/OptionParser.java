package com.geektime.tdd;

import com.geektime.tdd.exception.TooManyArgumentsException;

import java.util.List;

/**
 * OptionParser
 *
 * @author tengxq
 */
interface OptionParser<T> {

    T parse(List<String> arguments, Option option) throws TooManyArgumentsException;
}
