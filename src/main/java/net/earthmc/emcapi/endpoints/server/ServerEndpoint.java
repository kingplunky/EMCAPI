package net.earthmc.emcapi.endpoints.server;

import au.lupine.quarters.api.manager.QuarterManager;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.object.Resident;
import io.javalin.Javalin;
import io.minimum.minecraft.superbvote.SuperbVote;
import net.earthmc.emcapi.common.GetEndpoint;
import net.earthmc.emcapi.endpoints.server.object.*;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.time.LocalTime;

public class ServerEndpoint extends GetEndpoint<Server> {
    public ServerEndpoint(Javalin javalin) {
        super(javalin);
    }

    @Override
    public Server getResponse() {
        World overworld = Bukkit.getWorlds().get(0);
        TownyAPI townyAPI = TownyAPI.getInstance();
        QuarterManager quarterManager = QuarterManager.getInstance();

        int target = SuperbVote.getPlugin().getVoteParty().votesNeeded();
        int currentVotes = SuperbVote.getPlugin().getVoteParty().getCurrentVotes();

        return Server.builder()
                .version(Bukkit.getMinecraftVersion())
                .moonPhase(overworld.getMoonPhase().toString())
                .timestamps(ServerTimestamps.builder()
                        .newDayTime(TownySettings.getNewDayTime())
                        .serverTimeOfDay(LocalTime.now().toSecondOfDay())
                        .build())
                .status(ServerStatus.builder()
                        .hasStorm(overworld.hasStorm())
                        .isThundering(overworld.isThundering())
                        .build())

                .stats(ServerStats.builder()
                        .time(overworld.getTime())
                        .fullTime(overworld.getFullTime())
                        .maxPlayers(Bukkit.getMaxPlayers())
                        .numOnlinePlayers(Bukkit.getOnlinePlayers().size())
                        .numOnlineNomads(townyAPI.getResidentsWithoutTown().stream()
                                .filter(Resident::isOnline).count())
                        .numResidents(townyAPI.getResidents().size())
                        .numNomads(townyAPI.getResidentsWithoutTown().size())
                        .numTowns(townyAPI.getTowns().size())
                        .numTownBlocks(townyAPI.getTownBlocks().size())
                        .numNations(townyAPI.getNations().size())
                        .numQuarters(quarterManager.getAllQuarters().size())
                        .numCuboids(quarterManager.getAllQuarters().stream()
                                .mapToInt(quarter -> quarter.getCuboids().size()).sum())
                        .build())

                .voteParty(ServerVoteParty.builder()
                        .target(target)
                        .numRemaining(target - currentVotes)
                        .build())

                .build();
    }

    @Override
    public String getPath() {
        return "/";
    }
}
