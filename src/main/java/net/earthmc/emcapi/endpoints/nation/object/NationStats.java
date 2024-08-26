package net.earthmc.emcapi.endpoints.nation.object;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NationStats {
    private int numTownBlocks;
    private int numResidents;
    private int numTowns;
    private int numAllies;
    private int numEnemies;
    private double balance;
}
