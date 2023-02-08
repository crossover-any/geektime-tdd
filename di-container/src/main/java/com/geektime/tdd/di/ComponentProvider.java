package com.geektime.tdd.di;

/**
 * xxx
 *
 * @author tengxq
 */
public interface ComponentProvider<T> {

    T get(Context context);
}
