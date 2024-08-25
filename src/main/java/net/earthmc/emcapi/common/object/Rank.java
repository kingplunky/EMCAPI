package net.earthmc.emcapi.common.object;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Rank {
    private String name;
    private List<String> playerNames;
}
