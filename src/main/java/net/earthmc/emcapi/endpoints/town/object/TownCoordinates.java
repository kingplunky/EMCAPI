package net.earthmc.emcapi.endpoints.town.object;

import lombok.Builder;
import lombok.Getter;
import net.earthmc.emcapi.common.object.Spawn;

import java.util.List;

@Getter
@Builder
public class TownCoordinates {
    private Spawn spawn;
    private TownBlock homeBlock;
    private List<TownBlock> townBlocks;
}
