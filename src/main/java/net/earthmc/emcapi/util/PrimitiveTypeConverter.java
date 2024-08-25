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
        typeConverters.put(Boolean.class, Boolean::parseBoolean);
        typeConverters.put(boolean.class, Boolean::parseBoolean);
        typeConverters.put(Character.class, value -> value.length() == 1 ? value.charAt(0) : null);
        typeConverters.put(char.class, value -> value.length() == 1 ? value.charAt(0) : null);
        typeConverters.put(Short.class, Short::parseShort);
        typeConverters.put(short.class, Short::parseShort);
        typeConverters.put(Byte.class, Byte::parseByte);
        typeConverters.put(byte.class, Byte::parseByte);
        typeConverters.put(UUID.class, UUID::fromString);
        typeConverters.put(NullType.class, value -> null);

    }

    public Object castStringToType(String value, Class<?> targetType) {
        return typeConverters.get(targetType).apply(value);
    }

    public boolean isSupported(Class<?> type) {
        return typeConverters.containsKey(type);
    }

}