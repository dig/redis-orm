package com.github.dig.redis;

import com.github.dig.redis.annotation.Attribute;
import com.github.dig.redis.annotation.Id;
import com.github.dig.redis.exception.RedisMissingIdException;
import com.github.dig.redis.exception.RedisNoAttributeException;
import com.github.dig.redis.exception.RedisUnsupportedAttributeException;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class RedisValidator {

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
            throw new RedisMissingIdException();
        }

        if (attributeNum <= 0) {
            throw new RedisNoAttributeException();
        }
    }

    public static void checkValidAttribute(Field field) {
        Class<?> type = field.getType();
        if ((type.equals(Byte.class) || type.equals(byte.class))
                || type.equals(Character.class) || type.equals(char.class)
                || type.equals(Short.class) || type.equals(short.class)
                || type.equals(Integer.class) || type.equals(int.class)
                || type.equals(Float.class) || type.equals(float.class)
                || type.equals(Double.class) || type.equals(double.class)
                || type.equals(Long.class) || type.equals(long.class)
                || type.equals(Boolean.class) || type.equals(boolean.class)
                || type.equals(String.class)) {

        } else {
            throw new RedisUnsupportedAttributeException();
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

}
