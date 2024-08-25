package net.earthmc.emcapi.endpoints.quarter;

import com.palmergames.bukkit.towny.object.Resident;
import io.javalin.Javalin;
import net.earthmc.emcapi.common.Endpoint;
import net.earthmc.emcapi.endpoints.quarter.object.Quarter;
import net.earthmc.emcapi.endpoints.quarter.object.QuartersReference;

import java.util.List;

public class QuarterEndpoint extends Endpoint<Quarter> {
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
        return List.of();
    }

    public static QuartersReference createReference(au.lupine.quarters.object.entity.Quarter resident) {
        return QuartersReference.builder()
                .name(resident.getName())
                .uuid(resident.getUUID())
                .build();
    }
}
