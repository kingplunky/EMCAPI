package net.earthmc.emcapi.common.query;

import net.earthmc.emcapi.common.annotations.UseSuperClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FieldPathParser<T> {
    private final Class<?> type;
    private final int nestedFieldLimit;

    /**
     * @param type The root class from which the field path starts.
     * @param nestedFieldLimit The maximum number of nested fields to parse.
     */
    public FieldPathParser(Class<T> type, int nestedFieldLimit) {
        this.type = type;
        this.nestedFieldLimit = nestedFieldLimit;
    }

    /**
     * Parses a string representation of a field path into a list of `Field` objects.
     * This method takes a field path string (e.g. "status.hasTown") and converts it into a list of
     * `Field` objects that represent the fields in the specified path, starting from the provided class type.
     * It supports nested fields up to a defined limit, and it can handle cases where the target class
     * or one of its fields uses a superclass if it specifies `@UseSuperClass` annotation.
     *
     * @param fieldPath The string representing the field path, with fields separated by dots.
     * @return A list of `Field` objects corresponding to the field path.
     * @throws NoSuchFieldException If any part of the field path is invalid or cannot be found in the class hierarchy.
     */
    public List<Field> parse(String fieldPath) throws NoSuchFieldException {
        String[] stringFields = fieldPath.split("\\.");
        int iterations = Math.min(stringFields.length, nestedFieldLimit);

        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = type;

        for (int i = 0; i < iterations; i++) {
            String fieldName = stringFields[i];
            Field field = currentClass.getDeclaredField(fieldName);
            field.setAccessible(true);


            currentClass = field.getType().isAnnotationPresent(UseSuperClass.class) ?
                    field.getType().getSuperclass() : field.getType();

            fields.add(field);
        }

        return fields;
    }
}