package com.geektime.tdd;

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
        Option option = parameter.getAnnotation(Option.class);
        Object value = null;
        if (parameter.getType() == Boolean.class) {
            value = arguments.contains(option.value());
        }
        if (parameter.getType() == Integer.class) {
            int valueIndex = arguments.indexOf(option.value()) + 1;
            value = Integer.parseInt(arguments.get(valueIndex));
        }
        if (parameter.getType() == String.class) {
            int valueIndex = arguments.indexOf(option.value()) + 1;
            value = arguments.get(valueIndex);
        }
        return value;
    }
}
