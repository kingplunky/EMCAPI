package net.earthmc.emcapi.endpoints.town.object;
import lombok.Builder;
import lombok.Getter;
import net.earthmc.emcapi.common.object.Rank;
import net.earthmc.emcapi.common.object.perms.Permissions;
import net.earthmc.emcapi.endpoints.nation.object.NationReference;
import net.earthmc.emcapi.endpoints.player.object.PlayerReference;
import net.earthmc.emcapi.endpoints.quarter.object.Quarter;
import net.earthmc.emcapi.endpoints.quarter.object.QuartersReference;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class Town {
    private String name;
    private UUID uuid;
    private String board;
    private String founder;
    private String wiki;
    private PlayerReference mayor;
    private NationReference nation;
    private TownTimestamps timestamps;
    private TownStatus status;
    private TownStats stats;
    private Permissions permissions;
    private TownCoordinates coordinates;
    private List<PlayerReference> residents;
    private List<PlayerReference> trusted;
    private List<PlayerReference> outlaws;
    private List<QuartersReference> quarters;
    private List<Rank> ranks;
}
