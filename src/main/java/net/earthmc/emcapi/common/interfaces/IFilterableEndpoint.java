package net.earthmc.emcapi.common.interfaces;

import net.earthmc.emcapi.common.Filter;

import java.util.List;

public interface IFilterableEndpoint<T> extends IEndpoint {
    String getPath();

    List<T> createObjects();

    List<T> filter(List<Filter<T>> queries);
}
