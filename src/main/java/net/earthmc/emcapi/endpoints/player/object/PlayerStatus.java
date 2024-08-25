package net.earthmc.emcapi.endpoints.player.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
public class PlayerStatus {
    @JsonProperty("isOnline")
    private boolean isOnline;
    @JsonProperty("isNPC")
    private boolean isNPC;
    @JsonProperty("isMayor")
    private boolean isMayor;
    @JsonProperty("isKing")
    private boolean isKing;

    @JsonProperty("hasTown")
    @Getter private boolean hasTown;
    @JsonProperty("hasNation")
    @Getter private boolean hasNation;

    public boolean isKing() {
        return isKing;
    }

    public boolean isMayor() {
        return isMayor;
    }

    public boolean isNPC() {
        return isNPC;
    }

    public boolean isOnline() {
        return isOnline;
    }
}
