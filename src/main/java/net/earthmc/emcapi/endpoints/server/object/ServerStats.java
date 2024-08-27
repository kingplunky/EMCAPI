package net.earthmc.emcapi.endpoints.server.object;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerStats {
    private long time;
    private long fullTime;
    private int maxPlayers;
    private int numOnlinePlayers;
    private long numOnlineNomads;
    private int numResidents;
    private int numNomads;
    private int numTowns;
    private int numTownBlocks;
    private int numNations;
    private int numQuarters;
    private int numCuboids;
}
