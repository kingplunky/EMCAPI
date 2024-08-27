package net.earthmc.emcapi.util;

import com.palmergames.bukkit.towny.object.TownyPermission;
import net.earthmc.emcapi.common.object.Spawn;
import net.earthmc.emcapi.common.object.perms.PermissionArray;
import net.earthmc.emcapi.common.object.perms.PermissionFlags;
import net.earthmc.emcapi.common.object.perms.Permissions;
import net.earthmc.emcapi.endpoints.town.object.TownBlock;
import org.bukkit.Location;

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

    public static Spawn createSpawnObject(Location location) {
        if (location == null) return null;
        return Spawn.builder()
                .world(location.getWorld().getName())
                .x(location.getX())
                .y(location.getY())
                .z(location.getZ())
                .pitch(location.getPitch())
                .yaw(location.getYaw())
                .build();
    }

    public static TownBlock createTownBlock(com.palmergames.bukkit.towny.object.TownBlock townBlock) {
        if (townBlock == null) return null;
        return TownBlock.builder()
                .x(townBlock.getX())
                .z(townBlock.getZ())
                .build();
    }
}
