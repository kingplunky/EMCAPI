package net.earthmc.emcapi.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.*;

public class QueryDeserializer<T> extends JsonDeserializer<List<Query<T>>> {

    private final Class<T> clazz;
    private static final int MAX_EXPECTED_VALUES = 100;
    private static final int MAX_QUERIES = 5;

    public QueryDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<Query<T>> deserialize(JsonParser p, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonNode rootNode = p.getCodec().readTree(p);
        Map<String, Query<T>> queries = new HashMap<>();

        for (Iterator<String> it = rootNode.fieldNames(); it.hasNext(); ) {
            String field = it.next();
            JsonNode valueNode = rootNode.get(field);

            int size = valueNode.size();

            if (size > MAX_EXPECTED_VALUES) {
                throw new IOException(
                        String.format("Exceeded max expected values for query object: expected %s item(s) or less but got %s."
                        , MAX_EXPECTED_VALUES, size));
            }

            HashSet<String> expectedValues = extractExpectedValues(valueNode, size);

            if (queries.size() >= MAX_QUERIES) {
                throw new IOException("Exceeded maximum number of queries: " + MAX_QUERIES);
            }

            if (queries.containsKey(field)) {
                throw new IOException("Duplicate field: " + field);
            }

            queries.put(field, new Query<>(clazz, field, expectedValues));
        }

        return new ArrayList<>(queries.values());
    }

    private HashSet<String> extractExpectedValues(JsonNode valueNode, int size) {
        HashSet<String> expectedValues = new HashSet<>();


        if (valueNode.isArray()) {
            for (int i = 0; i < size; i++) {
                expectedValues.add(valueNode.get(i).asText());
            }
        } else {
            expectedValues.add(valueNode.asText());
        }

        return expectedValues;
    }
}
