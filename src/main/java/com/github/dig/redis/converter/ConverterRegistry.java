package com.github.dig.redis.converter;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class ConverterRegistry {

    @Getter
    private Set<Converter> converters = new HashSet<>();

    private ConverterRegistry() {
        register(new StringConverter());
        register(new IntegerConverter());
    }

    public void register(Converter converter) {
        converters.add(converter);
    }

    private static final ConverterRegistry instance = new ConverterRegistry();
    public static ConverterRegistry getInstance() {
        return instance;
    }
}
