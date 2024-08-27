package net.earthmc.emcapi.endpoints.server.object;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Server {
    private String version;
    private String moonPhase;
    private ServerTimestamps timestamps;
    private ServerStatus status;
    private ServerStats stats;
    private ServerVoteParty voteParty;
}
