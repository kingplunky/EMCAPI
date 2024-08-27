package net.earthmc.emcapi.endpoints.player;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import io.javalin.Javalin;
import net.earthmc.emcapi.common.Endpoint;
import net.earthmc.emcapi.util.EndpointUtil;
import net.earthmc.emcapi.endpoints.nation.NationEndpoint;
import net.earthmc.emcapi.endpoints.player.object.*;
import net.earthmc.emcapi.endpoints.town.TownEndpoint;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;

import java.util.List;

public class PlayerEndpoint extends Endpoint<Player> {
    private final Economy economy;

    public PlayerEndpoint(Javalin javalin, Economy economy) {
        super(javalin);
        clazz = this.reflectClassType();
        this.economy = economy;
    }

    @Override
    public String getPath() {
        return "/players";
    }

    @Override
    public List<Player> createObjects() {
        return TownyAPI.getInstance().getResidents()
                .stream().map(this::createObject).toList();
    }

    public Player createObject(Resident resident) {
        PlayerStats stats = PlayerStats.builder()
                .balance(economy.getBalance(Bukkit.getOfflinePlayer(resident.getUUID())))
                .numFriends(resident.getFriends().size())
                .build();

        return Player.builder()
                .name(resident.getName())
                .uuid(resident.getUUID())
                .title(resident.getTitle().isEmpty() ? null : resident.getTitle())
                .surname(resident.getSurname().isEmpty() ? null : resident.getSurname())
                .formattedName(resident.getFormattedName())
                .about(resident.getAbout().isEmpty() ? null : resident.getAbout())
                .town(TownEndpoint.createReference(resident.getTownOrNull()))
                .nation(NationEndpoint.createReference(resident.getNationOrNull()))

                .timestamps(PlayerTimestamps.builder()
                        .registered(resident.getRegistered())
                          .joinedTownAt(resident.hasTown() ? resident.getJoinedTownAt() : null)
                        .lastOnline(resident.getLastOnline() != 0 ? resident.getLastOnline() : null)
                        .build())

                .status(PlayerStatus.builder()
                        .isOnline(resident.isOnline())
                        .isNPC(resident.isNPC())
                        .isMayor(resident.isMayor())
                        .isKing(resident.isKing())
                        .hasTown(resident.hasTown())
                        .hasNation(resident.hasNation())
                        .build())

                .stats(stats)

                .permissions(EndpointUtil.createPermissionObject(resident.getPermissions()))

                .ranks(PlayerRanks.builder()
                        .townRanks(resident.getTownRanks())
                        .nationRanks(resident.getNationRanks())
                        .build())

                .friends(resident.getFriends().stream().map(PlayerEndpoint::createReference).toList())
                .build();
    }

    public static PlayerReference createReference(Resident resident) {
        return PlayerReference.builder()
                .name(resident.getName())
                .uuid(resident.getUUID())
                .build();
    }
}
