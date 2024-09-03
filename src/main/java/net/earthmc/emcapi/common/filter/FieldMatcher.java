package net.earthmc.emcapi.common.filter;

import net.earthmc.emcapi.common.Filter;

import java.lang.reflect.Field;
import java.util.*;

public class FieldMatcher<T> {
    /**
     * Determines if the value of the object's field matches any of the expected values.
     * This method compares the value of a specified field in the given object with a list
     * of expected values. If the field's value is `null` and `null` is one of the expected values,
     * it considers it a match. Otherwise, it attempts to match the field's actual value with the expected values.
     *
     * @param object The object whose field is being checked.
     * @param fields A list of fields representing the path to the target field within the object.
     * @param expectedValues A list of expected values to compare against the field's value.
     * @return True if the field's value matches any of the expected values; otherwise, false.
     */
    public boolean matches(T object, List<Field> fields, HashSet<String> expectedValues) {
        List<Object> fieldValues = retrieveFieldValues(object, fields);

        if (fieldValues.isEmpty() || fieldValues.contains(null)) {
            return containsNull(expectedValues);
        }

        return matchFieldValue(fieldValues, fields, expectedValues);
    }


    /**
     * Retrieves the value of the target field from the given object.
     * This method navigates through the object using the provided list of fields
     * to extract the value of the final target field. If at any point a `null`
     * value is encountered, the method returns `null`.
     *
     * @param object The object from which the field value is to be retrieved.
     * @param fields A list of fields representing the path to the target field within the object.
     * @return The value of the target field, or null if any intermediate field is null.
     */
    private List<Object> retrieveFieldValues(Object object, List<Field> fields) {
        List<Object> values = new ArrayList<>();

        Object currentValue = object;
        try {
            for (int i = 0 ; i < fields.size() ; i++) {
                if (currentValue == null) return new ArrayList<>();
                Field field = fields.get(i);

                if (List.class.isAssignableFrom(field.getType())) {
                    int finalI = i;
                    return ((List<Object>) field.get(currentValue)).stream()
                            .flatMap(o -> retrieveFieldValues(o, fields.subList(finalI + 1, fields.size())).stream())
                            .toList();
                }

                currentValue = field.get(currentValue);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access field value", e);
        }

        values.add(currentValue);

        return values;
    }

    /**
     * Checks if a `null` value is one of the expected values.
     * This method determines whether the list of expected values includes "null".
     *
     * @param expectedValues A list of expected values to be checked.
     * @return True if "null" is one of the expected values; otherwise, false.
     */
    private static boolean containsNull(HashSet<String> expectedValues) {
        return expectedValues.contains("null");
    }

    /**
     * Compares the actual field value with the list of expected values.
     * This method attempts to match the actual field value with each of the expected values.
     * It first casts each expected value to the field's type and then compares the cast
     * value with the actual field value.
     *
     * @param fieldValues The actual value of the field.
     * @param fields A list of fields representing the path to the target field within the object.
     * @param expectedValues A list of expected values to compare against the field's value.
     * @return True if the field's value matches any of the expected values; otherwise, false.
     */
    private static boolean matchFieldValue(List<Object> fieldValues, List<Field> fields, HashSet<String> expectedValues) {
        Class<?> fieldType = fields.get(fields.size() - 1).getType();

        for (String expectedValue : expectedValues) {
            if (expectedValue.equals("null")) continue;

            Object castedExpectedValue;

            try {
                castedExpectedValue = Filter.typeConverter.castStringToType(expectedValue, fieldType);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Failed to cast expected value to field type", e);
            }

            if (fieldValues.contains(castedExpectedValue)) {
                return true;
            }
        }

        return false;
    }
}

