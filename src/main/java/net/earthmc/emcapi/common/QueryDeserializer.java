package net.earthmc.emcapi.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class QueryDeserializer<T> extends JsonDeserializer<Query<T>> {

    private final Class<T> clazz;

    public QueryDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Query<T> deserialize(JsonParser p, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        if (node.size() != 1) {
            throw new IllegalArgumentException("Expected exactly one field in the JSON object");
        }

        String targetField = node.fieldNames().next();
        String expectedValue = node.get(targetField).asText();

        return new Query<>(clazz, targetField, expectedValue);
    }
}