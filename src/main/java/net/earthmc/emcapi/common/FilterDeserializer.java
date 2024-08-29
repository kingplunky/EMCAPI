package net.earthmc.emcapi.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import net.earthmc.emcapi.manager.EndpointManager;

import java.io.IOException;
import java.util.*;

public class FilterDeserializer<T> extends JsonDeserializer<List<Filter<T>>> {
    private final EndpointManager endpointManager;
    private final Class<T> clazz;

    public FilterDeserializer(Class<T> clazz) {
        this.clazz = clazz;
        this.endpointManager = EndpointManager.getInstance();
    }

    @Override
    public List<Filter<T>> deserialize(JsonParser p, DeserializationContext context) throws IOException {
        JsonNode rootNode = p.getCodec().readTree(p);
        Map<String, Filter<T>> filters = new HashMap<>();

        for (Iterator<String> it = rootNode.fieldNames(); it.hasNext();) {
            String field = it.next();
            JsonNode valueNode = rootNode.get(field);

            int size = valueNode.size();

            if (size > endpointManager.MAX_EXPECTED_VALUES) {
                throw new IOException(
                        String.format("Exceeded max expected values for filter object: expected %s item(s) or less but got %s."
                        , endpointManager.MAX_EXPECTED_VALUES, size));
            }

            HashSet<String> expectedValues = extractExpectedValues(valueNode, size);

            if (filters.size() >= endpointManager.MAX_FILTERS) {
                throw new IOException("Exceeded maximum number of filters: " + endpointManager.MAX_FILTERS);
            }

            if (filters.containsKey(field)) {
                throw new IOException("Duplicate field: " + field);
            }

            filters.put(field, new Filter<>(clazz, field, expectedValues));
        }

        return new ArrayList<>(filters.values());
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
