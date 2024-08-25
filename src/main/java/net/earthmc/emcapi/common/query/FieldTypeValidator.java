package net.earthmc.emcapi.common.query;


import net.earthmc.emcapi.util.PrimitiveTypeConverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FieldTypeValidator {
    private static final PrimitiveTypeConverter typeConverter = new PrimitiveTypeConverter();

    /**
     * Validates that each expected value is compatible with the target field type.
     * This method checks whether the expected values can be correctly cast to the
     * target field type. It also ensures that the field type is either a primitive
     * type, a supported type, or can handle null values if applicable.
     *
     * @param targetFieldType The type of the field that is being validated.
     * @param expectedValues A hashset of expected values as strings, which need to be validated against the field type.
     * @param fieldPath The string representation of the field's path within the parent class, used for error messages.
     * @param parentTypeName The name of the class that contains the field, used for constructing error messages.
     * @return A list of error messages, if any issues are found during validation. If no errors are found, the list will be empty.
     */
    public static List<String> validate(Class<?> targetFieldType, HashSet<String> expectedValues, String fieldPath, String parentTypeName) {
        List<String> errors = new ArrayList<>();
        boolean nullableField = !targetFieldType.isPrimitive();

        if (isTargetFieldSupportedType(targetFieldType, expectedValues)) {
            errors.add(String.format(
                    "%s.%s is not a primitive or supported type (e.g., String, int, float).",
                    parentTypeName, fieldPath
            ));
            return errors;
        }

        for (String expectedValue : expectedValues) {
            if (nullableField && "null".equals(expectedValue)) continue;

            try {
                typeConverter.castStringToType(expectedValue, targetFieldType);
            } catch (Exception e) {
                errors.add(String.format(
                        "Expected type '%s' for field path '%s.%s'",
                        targetFieldType.getSimpleName(), parentTypeName, fieldPath
                ));
            }
        }

        return errors;
    }

    private static boolean isTargetFieldSupportedType(Class<?> fieldType, HashSet<String> expectedValues) {
        return !typeConverter.isSupported(fieldType) && !expectedValues.contains("null");
    }

}
