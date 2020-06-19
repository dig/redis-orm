package com.github.dig.redis;

import com.github.dig.redis.annotation.Attribute;
import com.github.dig.redis.annotation.Id;
import com.github.dig.redis.converter.Converter;
import com.github.dig.redis.converter.ConverterRegistry;
import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@UtilityClass
public class RedisORM {

    public static void save(@NonNull Jedis jedis, Object object) {
        Class<?> clazz = object.getClass();
        Validator.checkValidClass(clazz);

        Optional<Field> idOptional = Validator.getId(clazz);
        idOptional.ifPresent(idField -> {
            Set<Field> attributes = Validator.getAttributes(clazz);

            ImmutableMap.Builder<String, String> builder = ImmutableMap.<String, String>builder();
            for (Field field : attributes) {
                builder.put(field.getName(), Validator.fromField(field, object));
            }

            jedis.hmset(String.format("%s:%s", clazz.getName(), Validator.fromField(idField, object)), builder.build());
        });
    }

    public static <T> T get(@NonNull Jedis jedis, Class<?> clazz, @NonNull Object id) {
        Validator.checkValidClass(clazz);

        Object object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Map<String, String> values = jedis.hgetAll(String.format("%s:%s", clazz.getName(), String.valueOf(id)));
        for (String key : values.keySet()) {
            String value = values.get(key);

            try {
                Field field = clazz.getField(key);
                setField(object, field, value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return (T) object;
    }

    private static Object convert(@NonNull Field field, @NonNull String value) {
        ConverterRegistry registry = ConverterRegistry.getInstance();
        Optional<Converter> converterOptional = registry.getConverters().stream()
                .filter(cvt -> cvt.canApply(field.getType()))
                .findFirst();

        if (converterOptional.isPresent()) {
            return converterOptional.get().from(value);
        }

        return null;
    }

    private static void setField(@NonNull Object newInstance, @NonNull Field field, @NonNull String value)
            throws IllegalAccessException {
        if (field.isAnnotationPresent(Attribute.class) || field.isAnnotationPresent(Id.class)) {
            field.setAccessible(true);
            field.set(newInstance, convert(field, value));
        }
    }

}
