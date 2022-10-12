package com.geektime.tdd;

import java.util.List;

/**
 * xxx
 *
 * @author tengxq
 */
class BooleanParser implements OptionParser {

    @Override
    public Object parse(List<String> arguments, Option option) {
        return arguments.contains(option.value());
    }
}
