package net.earthmc.emcapi.endpoints.quarter;

import au.lupine.quarters.api.manager.QuarterManager;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import io.javalin.Javalin;
import net.earthmc.emcapi.common.FilterableEndpoint;
import net.earthmc.emcapi.endpoints.player.PlayerEndpoint;
import net.earthmc.emcapi.endpoints.quarter.object.*;
import net.earthmc.emcapi.endpoints.town.TownEndpoint;
import org.bukkit.Location;
import java.awt.*;
import java.util.List;
import java.util.UUID;


public class QuarterEndpoint extends FilterableEndpoint<Quarter> {
    public QuarterEndpoint(Javalin javalin) {
        super(javalin);
        clazz = this.reflectClassType();
    }

    @Override
    public String getPath() {
        return "/quarters";
    }

    @Override
    public List<Quarter> createObjects() {
        return QuarterManager.getInstance().getAllQuarters()
                .stream().map(this::createObject).toList();
    }

    private Quarter createObject(au.lupine.quarters.object.entity.Quarter quarter) {
        TownyAPI api = TownyAPI.getInstance();
        UUID owner = quarter.getOwner();
        UUID creator = quarter.getCreator();

        Resident ownerResident = owner == null ? null : api.getResident(owner);
        Resident creatorResident = creator == null ? null : api.getResident(creator);

        Color color = quarter.getColour();

        return Quarter.builder()
                .name(quarter.getName())
                .uuid(quarter.getUUID())
                .type(quarter.getType().toString())
                .owner(ownerResident == null ? null : PlayerEndpoint.createReference(ownerResident))
                .creator(creatorResident == null ? null : PlayerEndpoint.createReference(creatorResident))

                .town(TownEndpoint.createReference(quarter.getTown()))
                .timestamps(QuarterTimestamps.builder()
                        .registered(quarter.getRegistered())
                        .claimedAt(quarter.getClaimedAt())
                        .build())
                .status(QuarterStatus.builder()
                        .isEmbassy(quarter.isEmbassy())
                        .isForSale(quarter.isForSale())
                        .build())
                .stats(QuarterStats.builder()
                        .price(quarter.getPrice())
                        .volume(quarter.getVolume())
                        .particleSize(quarter.getParticleSize())
                        .numCuboids(quarter.getCuboids().size())
                        .build())
                .color(QuarterColor.builder()
                        .red(color.getRed())
                        .green(color.getGreen())
                        .blue(color.getBlue())
                        .alpha(color.getAlpha())
                        .build())

                .trusted(quarter.getTrustedResidents()
                        .stream().map(PlayerEndpoint::createReference)
                        .toList())

                .cuboids(quarter.getCuboids().stream()
                        .map(cuboid -> {
                            Location cornerOne = cuboid.getCornerOne();
                            Location cornerTwo = cuboid.getCornerTwo();
                            return QuarterCuboid.builder()
                                    .cornerOne(QuarterCoordinate.builder()
                                            .x(cornerOne.getBlockX())
                                            .y(cornerOne.getBlockY())
                                            .z(cornerOne.getBlockZ())
                                            .build())
                                    .cornerTwo(QuarterCoordinate.builder()
                                            .x(cornerTwo.getBlockX())
                                            .y(cornerTwo.getBlockY())
                                            .z(cornerTwo.getBlockZ())
                                            .build())
                                    .build();
                        }).toList())
                .build();
    }

    public static QuartersReference createReference(au.lupine.quarters.object.entity.Quarter quarter) {
        return QuartersReference.builder()
                .name(quarter.getName())
                .uuid(quarter.getUUID())
                .build();
    }
}
