package net.earthmc.emcapi.endpoints.player.object;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlayerStatus {
    private boolean isOnline;
    private boolean isNPC;
    private boolean isMayor;
    private boolean isKing;
    private boolean hasTown;
    private boolean hasNation;
}
