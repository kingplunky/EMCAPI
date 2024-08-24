package net.earthmc.emcapi.common.interfaces;

import java.util.List;

public interface IEndpoint<T> {
    String getPath();
    List<T> query(IQuery<T> query);

    List<T> createObjects();
}
