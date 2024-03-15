package net.earthmc.emcapi.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyPermission;
import io.javalin.http.NotFoundResponse;
import net.earthmc.emcapi.EMCAPI;
import net.earthmc.emcapi.endpoint.NationsEndpoint;
import net.earthmc.emcapi.endpoint.PlayersEndpoint;
import net.earthmc.emcapi.endpoint.QuartersEndpoint;
import net.earthmc.emcapi.endpoint.TownsEndpoint;
import net.earthmc.quarters.api.QuartersAPI;
import net.earthmc.quarters.object.Quarter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class EndpointUtils {

    public static String lookup(String query, Function<String, ?> getObject, String notFoundMessage) {
        String[] split = query.split(",");
        JsonArray jsonArray = new JsonArray();

        for (int i = 0; i < Math.min(EMCAPI.instance.getConfig().getInt("behaviour.max_lookup_size"), split.length); i++) {
            String name = split[i];
            Object object = getObject.apply(name);

            if (object != null) {
                if (object instanceof Resident) {
                    jsonArray.add(PlayersEndpoint.getPlayerObject((Resident) object));
                } else if (object instanceof Town) {
                    jsonArray.add(TownsEndpoint.getTownObject((Town) object));
                } else if (object instanceof Nation) {
                    jsonArray.add(NationsEndpoint.getNationObject((Nation) object));
                } else if (object instanceof Quarter) {
                    jsonArray.add(QuartersEndpoint.getQuarterObject((Quarter) object));
                }
            } else {
                throw new NotFoundResponse(name + " " + notFoundMessage);
            }
        }

        return jsonArray.toString();
    }

    public static int getNumOnlineNomads() {
        int numOnlineNomads = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            Resident resident = TownyAPI.getInstance().getResident(player);
            if (resident != null && !resident.hasTown() && resident.isOnline()) {
                numOnlineNomads++;
            }
        }

        return numOnlineNomads;
    }

    public static Resident getResidentOrNull(String name) {
        Resident resident;

        try {
            resident = TownyAPI.getInstance().getResident(UUID.fromString(name));
        } catch (IllegalArgumentException e) {
            resident = TownyAPI.getInstance().getResident(name);
        }

        return resident;
    }

    public static Town getTownOrNull(String name) {
        Town town;

        try {
            town = TownyAPI.getInstance().getTown(UUID.fromString(name));
        } catch (IllegalArgumentException e) {
            town = TownyAPI.getInstance().getTown(name);
        }

        return town;
    }

    public static Nation getNationOrNull(String name) {
        Nation nation;

        try {
            nation = TownyAPI.getInstance().getNation(UUID.fromString(name));
        } catch (IllegalArgumentException e) {
            nation = TownyAPI.getInstance().getNation(name);
        }

        return nation;
    }

    public static Quarter getQuarterOrNull(String uuidString) {
        UUID uuid;

        try {
            uuid = UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            return null;
        }

        return QuartersAPI.getInstance().getQuarter(uuid);
    }

    public static JsonObject getPermsObject(TownyPermission permissions) {
        JsonObject permsObject = new JsonObject();

        permsObject.add("build", getPermArray(permissions, TownyPermission.ActionType.BUILD));
        permsObject.add("destroy", getPermArray(permissions, TownyPermission.ActionType.DESTROY));
        permsObject.add("switch", getPermArray(permissions, TownyPermission.ActionType.SWITCH));
        permsObject.add("itemUse", getPermArray(permissions, TownyPermission.ActionType.ITEM_USE));

        JsonObject flagsObject = new JsonObject();
        flagsObject.addProperty("pvp", permissions.pvp);
        flagsObject.addProperty("explosion", permissions.explosion);
        flagsObject.addProperty("fire", permissions.fire);
        flagsObject.addProperty("mobs", permissions.mobs);
        permsObject.add("flags", flagsObject);

        return permsObject;
    }

    private static JsonArray getPermArray(TownyPermission permissions, TownyPermission.ActionType actionType) {
        JsonArray jsonArray = new JsonArray();

        jsonArray.add(permissions.getPerm(TownyPermission.PermLevel.RESIDENT, actionType));
        jsonArray.add(permissions.getPerm(TownyPermission.PermLevel.NATION, actionType));
        jsonArray.add(permissions.getPerm(TownyPermission.PermLevel.ALLY, actionType));
        jsonArray.add(permissions.getPerm(TownyPermission.PermLevel.OUTSIDER, actionType));

        return jsonArray;
    }

    public static JsonObject getCoordinatesObject(@Nullable Location location) {
        JsonObject coordinatesObject = new JsonObject();

        JsonObject spawnObject = new JsonObject();
        if (location != null) {
            spawnObject.addProperty("world", location.getWorld().getName());
            spawnObject.addProperty("x", location.getX());
            spawnObject.addProperty("y", location.getY());
            spawnObject.addProperty("z", location.getZ());
            spawnObject.addProperty("pitch", location.getPitch());
            spawnObject.addProperty("yaw", location.getYaw());
            coordinatesObject.add("spawn", coordinatesObject);
        } else {
            coordinatesObject.add("spawn", null);
        }

        return coordinatesObject;
    }

    public static List<Resident> getNationRank(Nation nation, String rank) {
        List<Resident> residentsWithRank = new ArrayList<>();

        for (Resident resident : nation.getResidents()) {
            if (resident.hasNationRank(rank))
                residentsWithRank.add(resident);
        }

        return Collections.unmodifiableList(residentsWithRank);
    }

    public static JsonArray getResidentArray(List<Resident> residents) {
        JsonArray jsonArray = new JsonArray();

        for (Resident resident : residents) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", resident.getName());
            jsonObject.addProperty("uuid", resident.getUUID().toString());
            jsonArray.add(jsonObject);
        }

        return jsonArray.isEmpty() ? null : jsonArray;
    }

    public static JsonArray getTownArray(List<Town> towns) {
        JsonArray jsonArray = new JsonArray();

        for (Town town : towns) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", town.getName());
            jsonObject.addProperty("uuid", town.getUUID().toString());
            jsonArray.add(jsonObject);
        }

        return jsonArray.isEmpty() ? null : jsonArray;
    }

    public static JsonArray getNationArray(List<Nation> nations) {
        JsonArray jsonArray = new JsonArray();

        for (Nation nation : nations) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", nation.getName());
            jsonObject.addProperty("uuid", nation.getUUID().toString());
            jsonArray.add(jsonObject);
        }

        return jsonArray.isEmpty() ? null : jsonArray;
    }
}
