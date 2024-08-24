package net.earthmc.emcapi.common.object;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
public class Reference {
    private String name;
    private UUID uuid;
}
