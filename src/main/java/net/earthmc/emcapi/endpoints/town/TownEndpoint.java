package net.earthmc.emcapi.endpoints.town;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import io.javalin.Javalin;
import net.earthmc.emcapi.common.Endpoint;
import net.earthmc.emcapi.endpoints.player.object.PlayerReference;
import net.earthmc.emcapi.endpoints.town.object.Town;
import net.earthmc.emcapi.endpoints.town.object.TownReference;
import net.milkbowl.vault.economy.Economy;
import java.util.List;

public class TownEndpoint extends Endpoint<Town> {
    private final Economy economy;

    public TownEndpoint(Javalin javalin, Economy economy) {
        super(javalin);
        clazz = this.reflectClassType();
        this.economy = economy;
    }

    @Override
    public String getPath() {
        return "/player";
    }

    @Override
    public List<Town> createObjects() {
        return TownyAPI.getInstance().getTowns()
                .stream().map(this::createObject).toList();
    }

    private Town createObject(com.palmergames.bukkit.towny.object.Town town) {
        return Town.builder().build();
    }

    public static TownReference createReference(com.palmergames.bukkit.towny.object.Town town) {
        if (town == null) return null;

        return TownReference.builder()
                .name(town.getName())
                .uuid(town.getUUID())
                .build();
    }


}
