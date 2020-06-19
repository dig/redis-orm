package com.github.dig.redis;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class Converter {

    public static Object convert(Field field, String value) {
        return convert(field.getType(), value);
    }

    public static Object convert(Class<?> type, String value) {
        if (type.equals(Byte.class) || type.equals(byte.class)) {
            return new Byte(value);
        }

        if (type.equals(Short.class) || type.equals(short.class)) {
            return new Short(value);
        }

        if (type.equals(Integer.class) || type.equals(int.class)) {
            if (value == null) {
                return 0;
            }
            return new Integer(value);
        }

        if (type.equals(Float.class) || type.equals(float.class)) {
            if (value == null) {
                return 0f;
            }
            return new Float(value);
        }

        if (type.equals(Double.class) || type.equals(double.class)) {
            return new Double(value);
        }

        if (type.equals(Long.class) || type.equals(long.class)) {
            return new Long(value);
        }

        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return new Boolean(value);
        }

        return value;
    }

}
