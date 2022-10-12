package com.geektime.tdd;

import java.util.List;

/**
 * StringParser
 *
 * @author tengxq
 */
class StringParser extends IntParser {

    public StringParser() {
        super(String::valueOf);
    }
}
