package com.geektime.tdd;

import com.geektime.tdd.annotation.Option;
import com.geektime.tdd.service.BoolOptionParser;
import com.geektime.tdd.service.IntOptionParser;
import com.geektime.tdd.service.OptionParser;
import com.geektime.tdd.service.StringOptionParser;

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

    private static Object parseOption(List<String> arguments, Parameter parameter) {
        return PARSERS.get(parameter.getType()).parse(arguments, parameter.getDeclaredAnnotation(Option.class));
    }

    private static final Map<Class<?>, OptionParser> PARSERS = Map.of(
            Boolean.class, new BoolOptionParser(),
            Integer.class, new IntOptionParser(),
            String.class, new StringOptionParser()
    );

}
