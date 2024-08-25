package net.earthmc.emcapi.endpoints.town.object;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TownStats {
    private Integer numTownBlocks;
    private Integer maxTownBlocks;
    private Integer bonusBlocks;
    private Integer numResidents;
    private Integer numTrusted;
    private Integer numOutlaws;
    private Double balance;
    private Double forSalePrice;
}
