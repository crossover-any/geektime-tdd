package com.geektime.tdd.service;

import com.geektime.tdd.annotation.Option;

import java.util.List;

/**
 * @author tengxq
 */
public class BoolOptionParser implements OptionParser {

    @Override
    public Object parse(List<String> arguments, Option option) {
        return arguments.contains(option.value());
    }
}
