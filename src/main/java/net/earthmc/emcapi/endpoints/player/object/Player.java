package net.earthmc.emcapi.endpoints.player.object;

import lombok.Builder;
import lombok.Getter;
import net.earthmc.emcapi.common.object.perms.Permissions;
import net.earthmc.emcapi.endpoints.nation.object.NationReference;
import net.earthmc.emcapi.endpoints.town.object.TownReference;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Player {

    private String name;
    private UUID uuid;
    private String title;
    private String surname;
    private String formattedName;
    private String about;
    private TownReference town;
    private NationReference nation;
    private PlayerTimestamps timestamps;
    private PlayerStatus status;
    private PlayerStats stats;
    private Permissions permissions;
    private PlayerRanks ranks;
    private List<PlayerReference> friends;
}
