package net.earthmc.emcapi.common.object.perms;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PermissionFlags {
    private boolean pvp;
    private boolean explosion;
    private boolean fire;
    private boolean mobs;
}
