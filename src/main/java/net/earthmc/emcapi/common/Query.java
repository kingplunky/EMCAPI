package net.earthmc.emcapi.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import net.earthmc.emcapi.common.interfaces.IQuery;
import net.earthmc.emcapi.util.PrimitiveTypeConverter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Query<T> implements IQuery<T> {

    @Getter private final String fieldPath;
    @Getter private final String expectedValue;

    @JsonIgnore
    private final Class<T> type;
    @JsonIgnore
    private List<Field> fields = new ArrayList<>();
    @JsonIgnore
    private final PrimitiveTypeConverter typeConverter = new PrimitiveTypeConverter();

    public Query(Class<T> type, String fieldPath, String expectedValue) {
        this.type = type;
        this.fieldPath = fieldPath;
        this.expectedValue = expectedValue;
    }

    @Override
    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        try {
            fields = getFieldsFromPath(fieldPath);
            if (fields.isEmpty()) {
                errors.add("No field provided.");
                return errors;
            }

            Class<?> fieldType = fields.get(fields.size() - 1).getType();

            if (!typeConverter.isPrimitive(fieldType)) {
                errors.add(String.format(
                        "%s.%s is not a primitive or supported type (e.g., String, int, float).",
                        type.getSimpleName(), fieldPath
                ));
            }

            try {
                typeConverter.castStringToType(expectedValue, fieldType);
            } catch (Exception e) {
                errors.add(String.format(
                        "Expected type '%s' for field path '%s.%s'",
                        fieldType.getSimpleName(), type.getSimpleName(), fieldPath
                ));
            }

        } catch (NoSuchFieldException e) {
            errors.add(String.format(
                    "%s.%s is not a valid field.",
                    type.getSimpleName(), fieldPath
            ));
        }

        return errors;
    }

    private List<Field> getFieldsFromPath(String fieldPath) throws NoSuchFieldException {
        String[] stringFields = fieldPath.split("\\.");
        int iterations = Math.min(stringFields.length, NESTED_FIELD_LIMIT);

        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = type;

        for (int i = 0; i < iterations; i++) {
            String fieldName = stringFields[i];
            Field field = currentClass.getDeclaredField(fieldName);
            field.setAccessible(true);

            currentClass = field.getType();
            fields.add(field);
        }

        return fields;
    }

    @Override
    public boolean matches(T object) {
        if (fields.isEmpty()) {
            throw new IllegalStateException("Fields should not be empty. Ensure validation is called first.");
        }

        Object currentValue = object;

        try {
            for (Field field : fields) {
                if (currentValue == null) return false;
                field.setAccessible(true);
                currentValue = field.get(currentValue);
            }

            if (currentValue == null) return expectedValue == null;

            Class<?> fieldType = fields.get(fields.size() - 1).getType();
            Object castedExpectedValue = typeConverter.castStringToType(expectedValue, fieldType);

            return Objects.equals(currentValue, castedExpectedValue);

        } catch (IllegalAccessException | NumberFormatException e) {
            throw new RuntimeException("Failed to access field value or cast expected value", e);
        }
    }
}
