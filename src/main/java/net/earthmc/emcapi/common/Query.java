package net.earthmc.emcapi.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import net.earthmc.emcapi.common.annotations.UseSuperClass;
import net.earthmc.emcapi.common.interfaces.IQuery;
import net.earthmc.emcapi.common.query.FieldMatcher;
import net.earthmc.emcapi.common.query.FieldPathParser;
import net.earthmc.emcapi.common.query.FieldTypeValidator;
import net.earthmc.emcapi.util.PrimitiveTypeConverter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class Query<T> implements IQuery<T> {
    @Getter private final String fieldPath;
    @Getter private final HashSet<String> expectedValues;

    private final Class<T> type;
    private List<Field> fields = new ArrayList<>();
    private final FieldMatcher<T> fieldMatcher;
    private final FieldPathParser<T> fieldPathParser;
    public static final PrimitiveTypeConverter typeConverter = new PrimitiveTypeConverter();

    public Query(Class<T> type, String fieldPath, HashSet<String> expectedValues) {
        this.type = type;
        this.fieldPath = fieldPath;
        this.expectedValues = expectedValues;
        this.fieldMatcher = new FieldMatcher<>();
        this.fieldPathParser = new FieldPathParser<>(type, 10);
    }

    @Override
    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        try {
            fields = fieldPathParser.parse(fieldPath);
            if (fields.isEmpty()) {
                errors.add("No field provided.");
                return errors;
            }

            Class<?> fieldType = fields.get(fields.size() - 1).getType();
            errors.addAll(FieldTypeValidator.validate(fieldType, expectedValues, fieldPath, type.getSimpleName()));

        } catch (NoSuchFieldException e) {
            errors.add(String.format(
                    "%s.%s is not a valid field.",
                    type.getSimpleName(), fieldPath
            ));
        }

        return errors;
    }


    @Override
    public boolean matches(T object) {
        if (fields.isEmpty()) {
            throw new IllegalStateException("Fields should not be empty. Ensure validation is called first.");
        }

        return fieldMatcher.matches(object, fields, expectedValues);
    }
}
