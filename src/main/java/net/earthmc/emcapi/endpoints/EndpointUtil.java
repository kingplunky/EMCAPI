package net.earthmc.emcapi.endpoints;

import com.palmergames.bukkit.towny.object.TownyPermission;
import net.earthmc.emcapi.common.object.perms.PermissionArray;
import net.earthmc.emcapi.common.object.perms.PermissionFlags;
import net.earthmc.emcapi.common.object.perms.Permissions;

public class EndpointUtil {
    public static Permissions createPermissionObject(TownyPermission permissions) {
        return Permissions.builder()
                .canBuild(createPermissionArray(permissions, TownyPermission.ActionType.BUILD))
                .canDestroy(createPermissionArray(permissions, TownyPermission.ActionType.DESTROY))
                .canSwitch(createPermissionArray(permissions, TownyPermission.ActionType.SWITCH))
                .canItemUse(createPermissionArray(permissions, TownyPermission.ActionType.ITEM_USE))
                .flags(PermissionFlags.builder()
                        .pvp(permissions.pvp)
                        .explosion(permissions.explosion)
                        .fire(permissions.fire)
                        .mobs(permissions.mobs)
                        .build())
                .build();
    }

    private static PermissionArray createPermissionArray(TownyPermission permissions, TownyPermission.ActionType actionType) {
        return PermissionArray.builder()
                .resident(permissions.getPerm(TownyPermission.PermLevel.RESIDENT, actionType))
                .nation(permissions.getPerm(TownyPermission.PermLevel.NATION, actionType))
                .ally(permissions.getPerm(TownyPermission.PermLevel.ALLY, actionType))
                .outsider(permissions.getPerm(TownyPermission.PermLevel.OUTSIDER, actionType))
                .build();
    }
}
