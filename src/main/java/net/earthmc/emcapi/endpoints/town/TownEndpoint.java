package net.earthmc.emcapi.endpoints.town;
import au.lupine.quarters.api.manager.QuarterManager;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyEconomyHandler;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.permissions.TownyPerms;
import io.javalin.Javalin;
import net.earthmc.emcapi.common.FilterableEndpoint;
import net.earthmc.emcapi.common.object.Rank;
import net.earthmc.emcapi.util.EndpointUtil;
import net.earthmc.emcapi.endpoints.nation.NationEndpoint;
import net.earthmc.emcapi.endpoints.player.PlayerEndpoint;
import net.earthmc.emcapi.endpoints.quarter.QuarterEndpoint;
import net.earthmc.emcapi.endpoints.town.object.*;
import net.earthmc.emcapi.manager.TownMetadataManager;

import java.util.List;

public class TownEndpoint extends FilterableEndpoint<Town> {

    public TownEndpoint(Javalin javalin) {
        super(javalin);
        clazz = this.reflectClassType();
    }

    @Override
    public String getPath() {
        return "/towns";
    }

    @Override
    public List<Town> createObjects() {
        return TownyAPI.getInstance().getTowns()
                .stream().map(this::createObject).toList();
    }

    private Town createObject(com.palmergames.bukkit.towny.object.Town town) {
        return Town.builder()
                .name(town.getName())
                .uuid(town.getUUID())
                .board(town.getBoard().isEmpty() ? null : town.getBoard())
                .founder(town.getFounder())
                .wiki(TownMetadataManager.getWikiURL(town))
                .mayor(PlayerEndpoint.createReference(town.getMayor()))
                .nation(NationEndpoint.createReference(town.getNationOrNull()))

                .timestamps(TownTimestamps.builder()
                        .registered(town.getRegistered())
                        .joinedNationAt(town.hasNation() ? town.getJoinedNationAt() : null)
                        .ruinedAt(town.isRuined() ? town.getRuinedTime() : null)
                        .build())

                .status(TownStatus.builder()
                        .isPublic(town.isPublic())
                        .isOpen(town.isOpen())
                        .isNeutral(town.isNeutral())
                        .isCapital(town.isCapital())
                        .isOverClaimed(town.isOverClaimed())
                        .isRuined(town.isRuined())
                        .isForSale(town.isForSale())
                        .hasNation(town.hasNation())
                        .hasOverclaimShield(TownMetadataManager.hasOverclaimShield(town))
                        .canOutsidersSpawn(TownMetadataManager.getCanOutsidersSpawn(town))
                        .build())

                .permissions(EndpointUtil.createPermissionObject(town.getPermissions()))
                .stats(TownStats.builder()
                        .numTownBlocks(town.getNumTownBlocks())
                        .maxTownBlocks(town.getMaxTownBlocks())
                        .bonusBlocks(town.getBonusBlocks())
                        .numResidents(town.getNumResidents())
                        .numTrusted(town.getTrustedResidents().size())
                        .numOutlaws(town.getOutlaws().size())
                        .balance(TownyEconomyHandler.isActive() ? town.getAccount().getHoldingBalance() : 0)
                        .forSalePrice(!town.isForSale() ? null : town.getForSalePrice())
                        .build())

                .coordinates(TownCoordinates.builder()
                        .spawn(EndpointUtil.createSpawnObject(town.getSpawnOrNull()))
                        .homeBlock(EndpointUtil.createTownBlock(town.getHomeBlockOrNull()))
                        .townBlocks(town.getTownBlocks().stream().map(EndpointUtil::createTownBlock).toList())
                        .build())

                .residents(town.getResidents().stream().map(PlayerEndpoint::createReference).toList())
                .trusted(town.getTrustedResidents().stream().map(PlayerEndpoint::createReference).toList())
                .outlaws(town.getOutlaws().stream().map(PlayerEndpoint::createReference).toList())
                .quarters(QuarterManager.getInstance().getQuarters(town).stream().map(QuarterEndpoint::createReference)
                        .toList())

                .ranks(TownyPerms.getTownRanks().stream()
                        .map(rank -> Rank.builder()
                                .name(rank)
                                .playerNames(town.getRank(rank).stream()
                                        .map(Resident::getName)
                                        .toList())
                                .build())
                        .toList())
                .build();
    }

    public static TownReference createReference(com.palmergames.bukkit.towny.object.Town town) {
        if (town == null) return null;

        return TownReference.builder()
                .name(town.getName())
                .uuid(town.getUUID())
                .build();
    }


}
