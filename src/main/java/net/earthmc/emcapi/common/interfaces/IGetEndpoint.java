package net.earthmc.emcapi.common.interfaces;

public interface IGetEndpoint<T> extends IEndpoint {
    T getResponse();
}
