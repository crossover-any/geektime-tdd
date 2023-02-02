package com.geektime.tdd.di;

import jakarta.inject.Provider;

import java.util.HashMap;
import java.util.Map;

/**
 * 容器
 *
 * @author tengxq
 */
public class Context {

    private Map<Class<?>, Provider<?>> providers = new HashMap<>();
    public <T> void bind(Class<T> componentClass, T instance) {
        providers.put(componentClass, (Provider<T>) () -> instance);
    }


    public <T> T get(Class<T> componentClassType) {
        return (T) providers.get(componentClassType).get();
    }

    public <T, M extends T> void bind(Class<T> componentClass, Class<M> implementation) {
        providers.put(componentClass, (Provider<T>) () -> {
            try {
                return implementation.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
