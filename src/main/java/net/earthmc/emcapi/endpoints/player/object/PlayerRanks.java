package net.earthmc.emcapi.endpoints.player.object;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlayerRanks {
    private List<String> townRanks;
    private List<String> nationRanks;
}
