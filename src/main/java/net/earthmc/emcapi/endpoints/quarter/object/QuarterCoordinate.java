package net.earthmc.emcapi.endpoints.quarter.object;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuarterCoordinate {
    private int x;
    private int y;
    private int z;
}
