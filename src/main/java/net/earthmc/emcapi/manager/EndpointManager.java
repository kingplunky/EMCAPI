package net.earthmc.emcapi.manager;

import io.javalin.Javalin;
import lombok.Getter;
import net.earthmc.emcapi.common.FilterableEndpoint;
import net.earthmc.emcapi.common.interfaces.IEndpoint;
import net.earthmc.emcapi.endpoints.discord.DiscordEndpoint;
import net.earthmc.emcapi.endpoints.nation.NationEndpoint;
import net.earthmc.emcapi.endpoints.player.PlayerEndpoint;
import net.earthmc.emcapi.endpoints.quarter.QuarterEndpoint;
import net.earthmc.emcapi.endpoints.server.ServerEndpoint;
import net.earthmc.emcapi.endpoints.town.TownEndpoint;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class EndpointManager {
    @Getter private static EndpointManager instance;
    public static String BASE_URL;

    public final int MAX_EXPECTED_VALUES;
    public final int MAX_FILTERS;
    public final int PAGE_SIZE;

    private final List<IEndpoint> endpoints;

    public EndpointManager(Javalin javalin, FileConfiguration config, Economy economy) {
        instance = this;

        MAX_EXPECTED_VALUES = config.getInt("behaviour.filterable_endpoint.max_expected_values");
        MAX_FILTERS = config.getInt("behaviour.filterable_endpoint.max_filters");
        PAGE_SIZE = config.getInt("behaviour.filterable_endpoint.page_size");

        BASE_URL = "v3/" + config.getString("networking.url_path");

        endpoints = List.of(
                new PlayerEndpoint(javalin, economy),
                new TownEndpoint(javalin),
                new NationEndpoint(javalin),
                new QuarterEndpoint(javalin),
                new DiscordEndpoint(javalin),
                new ServerEndpoint(javalin)
        );
    }

    public void loadEndpoints() {
        for (IEndpoint endpoint : endpoints) {
            endpoint.setup();
        }
    }

}
