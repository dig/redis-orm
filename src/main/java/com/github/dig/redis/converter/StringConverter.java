package com.github.dig.redis.converter;

public class StringConverter implements Converter<String> {

    @Override
    public boolean canApply(Class<?> type) {
        return type.equals(String.class);
    }

    @Override
    public String to(String value) {
        return value;
    }

    @Override
    public String from(String value) {
        return value;
    }
}
