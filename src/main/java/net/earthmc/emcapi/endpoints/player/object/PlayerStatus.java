package net.earthmc.emcapi.endpoints.player.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;


@Builder
public class PlayerStatus {
    private boolean isOnline;
    private boolean isNPC;
    private boolean isMayor;
    private boolean isKing;

    @JsonProperty("hasTown")
    @Getter private boolean hasTown;
    @JsonProperty("hasNation")
    @Getter private boolean hasNation;

    public boolean getIsKing() {
        return isKing;
    }

    public boolean getIsMayor() {
        return isMayor;
    }

    public boolean getIsNPC() {
        return isNPC;
    }

    public boolean getIsOnline() {
        return isOnline;
    }
}
