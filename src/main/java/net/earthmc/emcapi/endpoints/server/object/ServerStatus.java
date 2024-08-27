package net.earthmc.emcapi.endpoints.server.object;

import lombok.Builder;
import lombok.Getter;


@Builder
public class ServerStatus {
    @Getter private boolean hasStorm;
    private boolean isThundering;

    public boolean isThundering() {
        return isThundering;
    }
}
