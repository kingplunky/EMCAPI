package  net.earthmc.emcapi.endpoints.discord.object;

import lombok.Builder;
import lombok.Getter;
import net.earthmc.emcapi.endpoints.player.object.PlayerReference;

@Builder
@Getter
public class Discord {
    private String discordId;
    private PlayerReference player;
}
