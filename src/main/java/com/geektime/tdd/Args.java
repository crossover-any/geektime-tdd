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
        Class<?> type = parameter.getType();
        OptionParser parser = null;
        if (type == Boolean.class) {
            parser = new BooleanParser();
        }
        if (type == Integer.class) {
            parser = new IntParser();
        }
        if (type == String.class) {
            parser = new StringParser();
        }
        return parser.parse(arguments, option);
    }

    interface OptionParser {

        Object parse(List<String> arguments, Option option);
    }

    static class BooleanParser implements OptionParser {

        @Override
        public Object parse(List<String> arguments, Option option) {
            return arguments.contains(option.value());
        }
    }

    static class IntParser implements OptionParser {
        @Override
        public Object parse(List<String> arguments, Option option) {
            int valueIndex = arguments.indexOf(option.value()) + 1;
            return Integer.parseInt(arguments.get(valueIndex));
        }
    }

    static class StringParser implements OptionParser {
        @Override
        public Object parse(List<String> arguments, Option option) {
            int valueIndex = arguments.indexOf(option.value()) + 1;
            return arguments.get(valueIndex);
        }
    }
}
