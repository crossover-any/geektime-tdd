package com.geektime.tdd;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object parseObject(Parameter parameter, List<String> arguments) {
        return PARSERS.get(parameter.getType()).parse(arguments, parameter.getAnnotation(Option.class));
    }

    private static final Map<Class<?>, OptionParser> PARSERS = Map.of(
            Boolean.class, new BooleanParser(),
            Integer.class, new SingleValueOptionParse<>(Integer::parseInt),
            String.class, new SingleValueOptionParse<>(String::valueOf)
    );

}
