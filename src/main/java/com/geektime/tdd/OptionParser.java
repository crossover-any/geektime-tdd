package com.geektime.tdd;

import java.util.List;

/**
 * OptionParser
 *
 * @author tengxq
 */
interface OptionParser {

    Object parse(List<String> arguments, Option option);
}
