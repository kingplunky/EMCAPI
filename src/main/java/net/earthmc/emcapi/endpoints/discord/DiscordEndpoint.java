package net.earthmc.emcapi.endpoints.discord;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.objects.managers.AccountLinkManager;
import io.javalin.Javalin;
import net.earthmc.emcapi.common.Endpoint;
import net.earthmc.emcapi.endpoints.discord.object.Discord;
import net.earthmc.emcapi.endpoints.player.PlayerEndpoint;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DiscordEndpoint extends Endpoint<Discord> {
    private final AccountLinkManager accountLinkManager = DiscordSRV.getPlugin().getAccountLinkManager();

    public DiscordEndpoint(Javalin javalin) {
        super(javalin);
    }

    @Override
    public String getPath() {
        return "/discord";
    }

    @Override
    public List<Discord> createObjects() {
        return accountLinkManager.getLinkedAccounts()
                .entrySet().stream().map(this::createObject).toList();
    }

    private Discord createObject(Map.Entry<String, UUID> entry) {
        Resident resident = TownyAPI.getInstance().getResident(entry.getValue());

        return Discord.builder()
                .discordId(entry.getKey())
                .player(resident == null ? null : PlayerEndpoint.createReference(resident))
                .build();
    }
}
