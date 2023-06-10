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
            List<String> arguments = Arrays.asList(args);
            Constructor<?> constructor = optionClass.getDeclaredConstructors()[0];
            Object[] values = Arrays.stream(constructor.getParameters()).map(p -> parseOption(arguments, p)).toArray();
            return (T) constructor.newInstance(values);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static Object parseOption(List<String> argsList, Parameter parameter) {
        Object value = null;
        Option option = parameter.getDeclaredAnnotation(Option.class);
        if (parameter.getType() == Boolean.class) {
            value = argsList.contains(option.value());
        } else if (parameter.getType() == Integer.class) {
           value = Integer.parseInt(argsList.get(argsList.indexOf(option.value()) + 1));
        } else if (parameter.getType() == String.class) {
            value = argsList.get(argsList.indexOf(option.value()) + 1);
        }
        return value;
    }
}
