package net.earthmc.emcapi.endpoints.quarter.object;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuarterStats {
    private Double price;
    private int volume;
    private int numCuboids;
    private Float particleSize;
}
