package com.geektime.tdd.di;

import jakarta.inject.Inject;
import jakarta.inject.Provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;

/**
 * 容器
 *
 * @author tengxq
 */
public class Context {

    private final Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public <T> void bind(Class<T> componentClass, T instance) {
        providers.put(componentClass, (Provider<T>) () -> instance);
    }


    public <T> T get(Class<T> componentClassType) {
        if (!providers.containsKey(componentClassType)) {
            throw new DependencyNotFoundException();
        }
        return (T) providers.get(componentClassType).get();
    }

    public <T, M extends T> void bind(Class<T> componentClass, Class<M> implementation) {
        //stream(implementation.getConstructors()).filter(c -> c.isAnnotationPresent(Inject.class)).co
        providers.put(componentClass, (Provider<T>) () -> {
                try {
                    Constructor<M> injectConstructor = getInjectConstructor(implementation);
                    Object[] dependencies = stream(injectConstructor.getParameters()).map(p -> get(p.getType())).toArray(Object[]::new);
                    return injectConstructor.newInstance(dependencies);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException  e) {
                    throw new IllegalComponentException();
                }
        });
    }

    @SuppressWarnings("uncheck")
    private <T> Constructor<T> getInjectConstructor(Class<T> implementation) {
        return (Constructor<T>) stream(implementation.getConstructors()).filter(c -> c.isAnnotationPresent(Inject.class))
                .findFirst().orElseGet(() -> {
                    try {
                        return implementation.getConstructor();
                    } catch (NoSuchMethodException e) {
                        throw new IllegalComponentException();
                    }
                });
    }
}
