package net.earthmc.emcapi.common.interfaces;

import net.earthmc.emcapi.common.Query;

import java.util.List;

public interface IFilterableEndpoint<T> extends IEndpoint {
    String getPath();

    List<T> createObjects();

    List<T> query(List<Query<T>> queries);
}
