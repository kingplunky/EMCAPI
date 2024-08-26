package net.earthmc.emcapi.endpoints.nation.object;

import lombok.Builder;

@Builder
public class NationStatus {
    private boolean isPublic;
    private boolean isOpen;
    private boolean isNeutral;

    public boolean getIsPublic() {
        return isPublic;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public boolean getIsNeutral() {
        return isNeutral;
    }
}
