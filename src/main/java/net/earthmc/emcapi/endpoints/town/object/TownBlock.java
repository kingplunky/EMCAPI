package net.earthmc.emcapi.endpoints.town.object;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TownBlock {
    private Integer x;
    private Integer z;
}
