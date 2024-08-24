package net.earthmc.emcapi.common.interfaces;

import java.util.List;

public interface IQuery<T> {
    int NESTED_FIELD_LIMIT = 10;

    List<String> validate();

    boolean matches(T object);
}
