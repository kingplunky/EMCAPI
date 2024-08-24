package net.earthmc.emcapi.endpoints.player.object;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlayerTimestamps {
    private Long registered;
    private Long joinedTownAt;
    private Long lastOnline;
}
