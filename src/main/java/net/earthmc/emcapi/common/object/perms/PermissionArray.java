package net.earthmc.emcapi.common.object.perms;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PermissionArray {
    private boolean resident;
    private boolean nation;
    private boolean ally;
    private boolean outsider;
}

