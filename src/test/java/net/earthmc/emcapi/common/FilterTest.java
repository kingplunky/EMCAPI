package net.earthmc.emcapi.common;


import net.earthmc.emcapi.endpoints.player.object.Player;
import net.earthmc.emcapi.endpoints.player.object.PlayerStats;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FilterTest {

    @Test
    public void validate_validFieldPath_shouldPass() {
        Filter<Player> playerFilter = new Filter<>(Player.class, "stats.numFriends", new HashSet<>(List.of("213")));

        List<String> errors = playerFilter.validate();

        assertTrue(errors.isEmpty(), "Expected no validation errors for valid field path");
    }

    @Test
    public void validate_invalidFieldPath_shouldReturnError() {
        Filter<Player> filter = new Filter<>(Player.class, "stats.invalidField", new HashSet<>(List.of("213")));

        List<String> errors = filter.validate();

        assertFalse(errors.isEmpty(), "Expected validation errors for invalid field path");
        assertEquals(1, errors.size(), "Expected exactly one validation error");
        assertTrue(errors.get(0).contains("invalidField"), "Expected error message to contain 'invalidField'");
    }

    @Test
    public void validate_primitiveFieldType_shouldPass() {
        Filter<Player> playerFilter = new Filter<>(Player.class, "stats.numFriends", new HashSet<>(List.of("213")));
        List<String> errors = playerFilter.validate();

        assertTrue(errors.isEmpty(), "Expected no validation errors for valid primitive field path");
    }

    @Test
    public void validate_nonPrimitiveFieldType_shouldReturnError() {
        Filter<Player> playerFilter = new Filter<>(Player.class, "stats", new HashSet<>(List.of("")));
        List<String> errors = playerFilter.validate();

        assertFalse(errors.isEmpty(), "Expected validation errors for non-primitive field type");
        assertEquals(1, errors.size(), "Expected exactly one validation error");
        assertTrue(errors.get(0).contains("stats"), "Expected error message to contain 'stats'");
    }

    @Test
    public void matches_validateNotCalled_shouldThrowException() {
        Filter<Player> playerFilter = new Filter<>(Player.class, "stats.numFriends", new HashSet<>(List.of("")));
        Player player = Player.builder()
                .stats(PlayerStats.builder()
                        .numFriends(10)
                        .build())
                .build();


        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            playerFilter.matches(player);
        });

        assertTrue(exception.getMessage().contains("Ensure validation is called first"),
                "Expected exception message to contain 'Ensure validation is called first'");

    }
}

