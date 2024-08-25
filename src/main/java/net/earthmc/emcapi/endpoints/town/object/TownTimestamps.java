package net.earthmc.emcapi.endpoints.town.object;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TownTimestamps {
    private Long registered;
    private Long joinedNationAt;
    private Long ruinedAt;
}
