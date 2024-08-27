package net.earthmc.emcapi.endpoints.nation;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyEconomyHandler;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.permissions.TownyPerms;
import io.javalin.Javalin;
import net.earthmc.emcapi.common.FilterableEndpoint;
import net.earthmc.emcapi.common.object.Rank;
import net.earthmc.emcapi.util.EndpointUtil;
import net.earthmc.emcapi.endpoints.nation.object.*;
import net.earthmc.emcapi.endpoints.player.PlayerEndpoint;
import net.earthmc.emcapi.endpoints.town.TownEndpoint;
import net.earthmc.emcapi.manager.NationMetadataManager;

import java.util.List;

public class NationEndpoint extends FilterableEndpoint<Nation> {
    public NationEndpoint(Javalin javalin) {
        super(javalin);
        clazz = this.reflectClassType();
    }

    @Override
    public String getPath() {
        return "/nations";
    }

    @Override
    public List<Nation> createObjects() {
        return TownyAPI.getInstance().getNations()
                .stream().map(this::createObject).toList();
    }

    private Nation createObject(com.palmergames.bukkit.towny.object.Nation nation) {
        return Nation.builder()
                .name(nation.getName())
                .uuid(nation.getUUID())
                .board(nation.getBoard().isEmpty() ? null : nation.getBoard())
                .dynmapColour(NationMetadataManager.getDynmapColour(nation))
                .dynmapOutline(NationMetadataManager.getDynmapOutline(nation))
                .wiki(NationMetadataManager.getWikiURL(nation))
                .king(PlayerEndpoint.createReference(nation.getKing()))
                .capital(TownEndpoint.createReference(nation.getCapital()))

                .timestamps(NationTimestamps.builder()
                        .registered(nation.getRegistered())
                        .build())

                .status(NationStatus.builder()
                        .isPublic(nation.isPublic())
                        .isNeutral(nation.isNeutral())
                        .isOpen(nation.isOpen())
                        .build())

                .stats(NationStats.builder()
                        .numTownBlocks(nation.getNumTownblocks())
                        .numResidents(nation.getNumResidents())
                        .numTowns(nation.getNumTowns())
                        .numAllies(nation.getAllies().size())
                        .numEnemies(nation.getEnemies().size())
                        .balance(TownyEconomyHandler.isActive() ? nation.getAccount().getHoldingBalance() : 0)
                        .build())

                .coordinates(NationCoordinates.builder()
                        .spawn(EndpointUtil.createSpawnObject(nation.getSpawnOrNull()))
                        .build())

                .residents(nation.getResidents().stream().map(PlayerEndpoint::createReference).toList())

                .towns(nation.getTowns().stream().map(TownEndpoint::createReference).toList())

                .allies(nation.getAllies().stream().map(NationEndpoint::createReference).toList())

                .enemies(nation.getEnemies().stream().map(NationEndpoint::createReference).toList())

                .sanctioned(nation.getSanctionedTowns().stream().map(TownEndpoint::createReference).toList())

                .ranks(TownyPerms.getNationRanks().stream()
                        .map(rank -> Rank.builder()
                                .name(rank)
                                .playerNames(nation.getResidents().stream()
                                        .filter(resident -> resident.hasNationRank(rank))
                                        .map(Resident::getName)
                                        .toList())
                                .build())
                        .toList())
                .build();

    }

    public static NationReference createReference(com.palmergames.bukkit.towny.object.Nation nation) {
        if (nation == null) return null;

        return NationReference.builder()
                .name(nation.getName())
                .uuid(nation.getUUID())
                .build();
    }


}
