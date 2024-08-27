package net.earthmc.emcapi.endpoints.server.object;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServerTimestamps {
    private long newDayTime;
    private int serverTimeOfDay;
}
