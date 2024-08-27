package net.earthmc.emcapi.endpoints.quarter.object;

import lombok.Builder;
import lombok.Getter;
import net.earthmc.emcapi.endpoints.nation.object.NationReference;
import net.earthmc.emcapi.endpoints.player.object.PlayerReference;
import net.earthmc.emcapi.endpoints.town.object.TownReference;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Quarter {
    private String name;
    private UUID uuid;
    private String type;
    private PlayerReference owner;
    private PlayerReference creator;
    private TownReference town;
    private NationReference nation;
    private QuarterTimestamps timestamps;
    private QuarterStatus status;
    private QuarterStats stats;
    private QuarterColor color;
    private List<PlayerReference> trusted;
    private List<QuarterCuboid> cuboids;
}
