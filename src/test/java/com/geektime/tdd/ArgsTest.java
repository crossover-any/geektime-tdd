package com.geektime.tdd;

/**
 * ArgsTest
 *
 * @author tengxq
 */
public class ArgsTest {

    // -l -p 8080 -d /usr/logs
    // Happy Path
    // Single Option
    // TODO bool -l
    // TODO int -p 8080
    // TODO str -d /usr/logs
    // TODO Multi options -l -p 8080 -d /usr/logs
    // Sad Path
    // TODO -bool -l t或者 -l t f
    // TODO -int -p 或者 -p 8080 8081
    // TODO -str -d 或者 -d /usr/logs /usr/vars
    // Default Value
    // TODO -bool false
    // TODO -int 0
    // TODO -str ""
}
