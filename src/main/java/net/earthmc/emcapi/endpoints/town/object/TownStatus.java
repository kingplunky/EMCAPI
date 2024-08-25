package net.earthmc.emcapi.endpoints.town.object;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TownStatus {
    private boolean isPublic;
    private boolean isOpen;
    private boolean isNeutral;
    private boolean isCapital;
    private boolean isOverClaimed;
    private boolean isRuined;
    private boolean isForSale;
    private boolean hasNation;
    private boolean hasOverclaimShield;
    private boolean canOutsidersSpawn;
}
