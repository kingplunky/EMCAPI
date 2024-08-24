package net.earthmc.emcapi.endpoints.player.object;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlayerStats {
    private double balance;
    private int numFriends;
}

