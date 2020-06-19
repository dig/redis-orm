package com.github.dig.redis.converter;

public class IntegerConverter implements Converter<Integer> {

    @Override
    public boolean canApply(Class<?> type) {
        return type.equals(int.class) || type.equals(Integer.class);
    }

    @Override
    public String to(Integer value) {
        return String.valueOf(value);
    }

    @Override
    public Integer from(String value) {
        return Integer.parseInt(value);
    }
}
