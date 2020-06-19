package com.github.dig.redis.converter;

public interface Converter<T> {

    boolean canApply(Class<?> type);
    String to(T value);
    T from (String value);

}
