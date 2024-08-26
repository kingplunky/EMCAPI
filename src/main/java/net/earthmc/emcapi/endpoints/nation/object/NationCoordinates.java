package net.earthmc.emcapi.endpoints.nation.object;

import lombok.Builder;
import lombok.Getter;
import net.earthmc.emcapi.common.object.Spawn;

@Getter
@Builder
public class NationCoordinates {
    private Spawn spawn;
}
