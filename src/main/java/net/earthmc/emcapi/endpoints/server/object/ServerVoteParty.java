package net.earthmc.emcapi.endpoints.server.object;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerVoteParty {
    private int target;
    private int numRemaining;
}
