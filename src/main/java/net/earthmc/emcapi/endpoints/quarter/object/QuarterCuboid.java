package net.earthmc.emcapi.endpoints.quarter.object;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class QuarterCuboid {
    private QuarterCoordinate cornerOne;
    private QuarterCoordinate cornerTwo;
}
