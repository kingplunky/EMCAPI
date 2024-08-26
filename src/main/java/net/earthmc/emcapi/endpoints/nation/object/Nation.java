package net.earthmc.emcapi.endpoints.nation.object;
import lombok.Builder;
import lombok.Getter;
import net.earthmc.emcapi.common.object.Rank;
import net.earthmc.emcapi.endpoints.player.object.PlayerReference;
import net.earthmc.emcapi.endpoints.town.object.TownReference;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Nation {
    private String name;
    private UUID uuid;
    private String board;
    private String dynmapColour;
    private String dynmapOutline;
    private String wiki;
    private PlayerReference king;
    private TownReference capital;
    private NationTimestamps timestamps;
    private NationStatus status;
    private NationStats stats;
    private NationCoordinates coordinates;
    private List<PlayerReference> residents;
    private List<TownReference> towns;
    private List<NationReference> allies;
    private List<NationReference> enemies;
    private List<TownReference> sanctioned;
    private List<Rank> ranks;
}
