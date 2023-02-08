package com.geektime.tdd.di;

/**
 * xxx
 *
 * @author tengxq
 */
public interface Context {
    <T> T get(Class<T> componentClassType);
}
