package net.earthmc.emcapi.endpoints.nation;

import com.palmergames.bukkit.towny.TownyAPI;
import io.javalin.Javalin;
import net.earthmc.emcapi.common.Endpoint;
import net.earthmc.emcapi.endpoints.nation.object.Nation;
import net.earthmc.emcapi.endpoints.nation.object.NationReference;
import net.milkbowl.vault.economy.Economy;

import java.util.List;

public class NationEndpoint extends Endpoint<Nation> {
    private final Economy economy;

    public NationEndpoint(Javalin javalin, Economy economy) {
        super(javalin);
        clazz = this.reflectClassType();
        this.economy = economy;
    }

    @Override
    public String getPath() {
        return "/player";
    }

    @Override
    public List<Nation> createObjects() {
        return TownyAPI.getInstance().getNations()
                .stream().map(this::createObject).toList();
    }

    private Nation createObject(com.palmergames.bukkit.towny.object.Nation nation) {
        return Nation.builder().build();
    }

    public static NationReference createReference(com.palmergames.bukkit.towny.object.Nation nation) {
        if (nation == null) return null;

        return NationReference.builder()
                .name(nation.getName())
                .uuid(nation.getUUID())
                .build();
    }


}
