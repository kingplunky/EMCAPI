package net.earthmc.emcapi.common.interfaces;

import net.earthmc.emcapi.common.Query;

import java.util.List;

public interface IEndpoint<T> {
    String getPath();

    List<T> createObjects();

    List<T> query(List<Query<T>> queries);
}
