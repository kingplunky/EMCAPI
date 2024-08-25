package net.earthmc.emcapi.common.interfaces;

import java.util.List;

public interface IQuery<T> {
    List<String> validate();

    boolean matches(T object);
}
