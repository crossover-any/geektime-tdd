package com.geektime.tdd;

import com.geektime.tdd.exception.IllegalOptionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Args
 *
 * @author tengxq
 */
public class Args {

    private Args() {

    }

    @SuppressWarnings({"unchecked"})
    public static <T> T parse(Class<T> optionsClass, String... args) {
        Constructor<?> declaredConstructor = optionsClass.getDeclaredConstructors()[0];
        List<String> arguments = Arrays.asList(args);

        Parameter[] parameters = declaredConstructor.getParameters();
        Object[] values = Arrays.stream(parameters).map(parameter -> parseObject(parameter, arguments)).toArray();
        try {
            return ((T) declaredConstructor.newInstance(values));
        } catch (IllegalOptionException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked"})
    private static Object parseObject(Parameter parameter, List<String> arguments) {
        if (!parameter.isAnnotationPresent(Option.class)) {
            throw new IllegalOptionException(parameter.getName());
        }
        return PARSERS.get(parameter.getType()).parse(arguments, parameter.getAnnotation(Option.class));
    }

    private static final Map<Class<?>, OptionParser> PARSERS = Map.of(
            Boolean.class, OptionParsers.bool(),
            Integer.class, OptionParsers.unary(0, Integer::parseInt),
            String.class, OptionParsers.unary("", String::valueOf)
    );

}
