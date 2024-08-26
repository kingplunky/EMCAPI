package net.earthmc.emcapi.manager;

import io.javalin.Javalin;
import net.earthmc.emcapi.common.Endpoint;
import net.earthmc.emcapi.endpoints.nation.NationEndpoint;
import net.earthmc.emcapi.endpoints.player.PlayerEndpoint;
import net.earthmc.emcapi.endpoints.town.TownEndpoint;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class EndpointManager {

    public static String BASE_URL;
    private final List<Endpoint<?>> endpoints;

    public EndpointManager(Javalin javalin, FileConfiguration config, Economy economy) {
        BASE_URL = "v3/" + config.getString("networking.url_path");

        endpoints = new ArrayList<>(List.of(
                new PlayerEndpoint(javalin, economy),
                new TownEndpoint(javalin),
                new NationEndpoint(javalin)
        ));
    }

    public void loadEndpoints() {
        for (Endpoint<?> endpoint : endpoints) {
            endpoint.setup();
        }
    }
}
