package net.earthmc.emcapi.common.object;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Spawn {
    private String world;
    private Double x;
    private Double y;
    private Double z;
    private Float pitch;
    private Float yaw;
}
