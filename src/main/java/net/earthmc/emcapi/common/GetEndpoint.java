package net.earthmc.emcapi.common;

import io.javalin.Javalin;
import net.earthmc.emcapi.common.interfaces.IEndpoint;
import net.earthmc.emcapi.common.interfaces.IGetEndpoint;
import net.earthmc.emcapi.manager.EndpointManager;

public abstract class GetEndpoint<T> implements IGetEndpoint<T> {
    private final Javalin javalin;

    public GetEndpoint(Javalin javalin) {
        this.javalin = javalin;
    }

    @Override
    public void setup() {
        javalin.get(EndpointManager.BASE_URL + this.getPath(), ctx -> {
             ctx.json(this.getResponse());
        });
    }
}
