package net.earthmc.emcapi.util;

import javax.lang.model.type.NullType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class PrimitiveTypeConverter {
    private final Map<Class<?>, Function<String, ?>> typeConverters = new HashMap<>();

    public PrimitiveTypeConverter() {
        typeConverters.put(String.class, Function.identity());
        typeConverters.put(Integer.class, Integer::parseInt);
        typeConverters.put(int.class, Integer::parseInt);
        typeConverters.put(Long.class, Long::parseLong);
        typeConverters.put(long.class, Long::parseLong);
        typeConverters.put(Double.class, Double::parseDouble);
        typeConverters.put(double.class, Double::parseDouble);
        typeConverters.put(Float.class, Float::parseFloat);
        typeConverters.put(float.class, Float::parseFloat);
        typeConverters.put(Boolean.class, this::strictParseBoolean);
        typeConverters.put(boolean.class, this::strictParseBoolean);
        typeConverters.put(Character.class, value -> value.length() == 1 ? value.charAt(0) : null);
        typeConverters.put(char.class, value -> value.length() == 1 ? value.charAt(0) : null);
        typeConverters.put(Short.class, Short::parseShort);
        typeConverters.put(short.class, Short::parseShort);
        typeConverters.put(Byte.class, Byte::parseByte);
        typeConverters.put(byte.class, Byte::parseByte);
        typeConverters.put(UUID.class, UUID::fromString);
    }

    public <T> T castStringToType(String value, Class<T> targetType) {
        return (T) typeConverters.get(targetType).apply(value);
    }

    public boolean isSupported(Class<?> type) {
        return typeConverters.containsKey(type);
    }

    private boolean strictParseBoolean(String value) {
        if ("true".equalsIgnoreCase(value)) {
            return true;
        } else if ("false".equalsIgnoreCase(value)) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid boolean value: " + value + ". Only 'true' or 'false' are allowed.");
        }
    }

}