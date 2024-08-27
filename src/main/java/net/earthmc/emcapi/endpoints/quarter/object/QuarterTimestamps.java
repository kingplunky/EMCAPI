package net.earthmc.emcapi.endpoints.quarter.object;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuarterTimestamps {
    private long registered;
    private Long claimedAt;
}
