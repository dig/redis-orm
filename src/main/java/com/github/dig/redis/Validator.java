package com.github.dig.redis;

import com.github.dig.redis.annotation.Attribute;
import com.github.dig.redis.annotation.Id;
import com.github.dig.redis.converter.Converter;
import com.github.dig.redis.converter.ConverterRegistry;
import com.github.dig.redis.exception.MissingIdException;
import com.github.dig.redis.exception.NoAttributeException;
import com.github.dig.redis.exception.UnsupportedAttributeException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class Validator {

    public static void checkValidClass(Class<?> clazz) {
        int attributeNum = 0;
        boolean hasId = false;

        for (Field field : clazz.getFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                checkValidAttribute(field);
                hasId = true;
            }

            if (field.isAnnotationPresent(Attribute.class)) {
                checkValidAttribute(field);
                attributeNum++;
            }
        }

        if (!hasId) {
            throw new MissingIdException();
        }

        if (attributeNum <= 0) {
            throw new NoAttributeException();
        }
    }

    public static void checkValidAttribute(@NonNull Field field) {
        Class<?> type = field.getType();
        ConverterRegistry registry = ConverterRegistry.getInstance();
        Optional<Converter> converterOptional = registry.getConverters().stream()
                .filter(cvt -> cvt.canApply(type))
                .findFirst();

        if (!converterOptional.isPresent()) {
            throw new UnsupportedAttributeException();
        }
    }

    public static Optional<Field> getId(Class<?> clazz) {
        for (Field field : clazz.getFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return Optional.of(field);
            }
        }

        return Optional.empty();
    }

    public static Set<Field> getAttributes(Class<?> clazz) {
        return Arrays.stream(clazz.getFields())
                .filter(field -> field.isAnnotationPresent(Attribute.class))
                .collect(Collectors.toSet());
    }

    public static String fromField(@NonNull Field field, @NonNull Object instance) {
        checkValidAttribute(field);
        Object value = null;
        try {
            value = field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return String.valueOf(value);
    }

}
