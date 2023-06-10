package com.geektime.tdd.service;

import com.geektime.tdd.annotation.Option;

import java.util.List;

/**
 * @author tengxq
 */
public class StringOptionParser implements OptionParser {

    @Override
    public Object parse(List<String> arguments, Option option) {
        return arguments.get(arguments.indexOf(option.value()) + 1);
    }
}
