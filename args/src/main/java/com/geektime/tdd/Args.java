package com.geektime.tdd;

import com.geektime.tdd.annotation.Option;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

/**
 * Args
 *
 * @author tengxq
 */
public class Args {

    private Args() {

    }

    @SuppressWarnings("unchecked")
    public static <T> T parse(Class<T> optionClass, String... args) {
        try {
            List<String> argsList = Arrays.asList(args);
            Constructor<?> constructor = optionClass.getDeclaredConstructors()[0];
            Parameter parameter = constructor.getParameters()[0];
            Object value = null;
            Option option = parameter.getDeclaredAnnotation(Option.class);
            if (parameter.getType() == Boolean.class) {
                value = argsList.contains(option.value());
            } else if (parameter.getType() == Integer.class) {
               value = Integer.parseInt(argsList.get(argsList.indexOf(option.value()) + 1));
                return (T) constructor.newInstance(value);
            } else if (parameter.getType() == String.class) {
                value = argsList.get(argsList.indexOf(option.value()) + 1);
            }
            return (T) constructor.newInstance(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
