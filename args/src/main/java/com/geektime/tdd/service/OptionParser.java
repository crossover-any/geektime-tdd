package com.geektime.tdd.service;

import com.geektime.tdd.annotation.Option;

import java.util.List;

/**
 * @author tengxq
 */
public interface OptionParser {
    Object parse(List<String> arguments, Option option);
}
