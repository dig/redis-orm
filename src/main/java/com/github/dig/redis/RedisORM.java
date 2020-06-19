package com.github.dig.redis;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;

@UtilityClass
public class RedisORM {

    public static void save(@NonNull Jedis jedis, Class<?> clazz) {
        RedisValidator.checkValidClass(clazz);

        Optional<Field> id = RedisValidator.getId(clazz);
        Set<Field> attributes = RedisValidator.getAttributes(clazz);

        
    }

}
