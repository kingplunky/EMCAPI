package net.earthmc.emcapi.endpoints.quarter.object;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuarterColor {
    private int red;
    private int green;
    private int blue;
    private int alpha;
}
