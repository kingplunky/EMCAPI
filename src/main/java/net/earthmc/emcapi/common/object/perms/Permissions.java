package net.earthmc.emcapi.common.object.perms;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Permissions {
    private PermissionArray canBuild;
    private PermissionArray canDestroy;
    private PermissionArray canSwitch;
    private PermissionArray canItemUse;
    private PermissionFlags flags;
}
