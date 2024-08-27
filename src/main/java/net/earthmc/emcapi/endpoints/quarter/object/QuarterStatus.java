package net.earthmc.emcapi.endpoints.quarter.object;

import lombok.Builder;

@Builder
public class QuarterStatus {
    private boolean isEmbassy;
    private boolean isForSale;

    public boolean getIsForSale() {
        return isForSale;
    }

    public boolean getIsEmbassy() {
        return isEmbassy;
    }
}
