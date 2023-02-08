package com.geektime.tdd.di;

import jakarta.inject.Inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

/**
 * 容器
 *
 * @author tengxq
 */
public class ContextConfig {

    private final Map<Class<?>, ComponentProvider<?>> providers = new HashMap<>();

    private final Map<Class<?>, List<Class<?>>> dependencies = new HashMap<>();

    public <T> void bind(Class<T> componentClass, T instance) {
        providers.put(componentClass, context -> instance);
        dependencies.put(componentClass, new ArrayList<>());
    }

    public <T, M extends T> void bind(Class<T> componentClass, Class<M> implementation) {
        Constructor<M> injectConstructor = getInjectConstructor(implementation);
        providers.put(componentClass, new ConstructorProvider<>(implementation, injectConstructor));
        dependencies.put(componentClass, stream(injectConstructor.getParameters()).map(Parameter::getType).collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    public Context getContext() {
        dependencies.keySet().forEach(d -> checkDependencies(d, new ArrayDeque<>()));
        return new Context() {
            @Override
            public <T> T get(Class<T> componentClassType) {
                if (!providers.containsKey(componentClassType)) {
                    throw new DependencyNotFoundException();
                }
                return (T) providers.get(componentClassType).get(this);
            }
        };
    }

    private void checkDependencies(Class<?> componentClass, Deque<Class<?>> visiting) {
        for (Class<?> dependency : dependencies.get(componentClass)) {
            if (!dependencies.containsKey(dependency)) {
                throw new DependencyNotFoundException();
            }
            if (visiting.contains(dependency)) {
                throw new CyclicDependencyException();
            }
            visiting.push(dependency);
            checkDependencies(dependency, visiting);
            visiting.pop();
        }

    }


    static class ConstructorProvider<T> implements ComponentProvider<T> {


        Class<T> implementation;

        Constructor<T> injectConstructor;

        public ConstructorProvider(Class<T> implementation, Constructor<T> injectConstructor) {
            this.implementation = implementation;
            this.injectConstructor = injectConstructor;
        }

        @Override
        public T get(Context context) {
            try {
                Object[] dependencies = stream(injectConstructor.getParameters()).map(p -> context.get(p.getType())).toArray(Object[]::new);
                return injectConstructor.newInstance(dependencies);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalComponentException();
            }
        }


    }


    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> getInjectConstructor(Class<T> implementation) {
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
