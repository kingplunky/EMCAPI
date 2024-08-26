package net.earthmc.emcapi.common;


import net.earthmc.emcapi.endpoints.player.object.Player;
import net.earthmc.emcapi.endpoints.player.object.PlayerStats;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class QueryTest {

    @Test
    public void validate_validFieldPath_shouldPass() {
        Query<Player> playerQuery = new Query<>(Player.class, "stats.numFriends", new HashSet<>(List.of("213")));

        List<String> errors = playerQuery.validate();

        assertTrue(errors.isEmpty(), "Expected no validation errors for valid field path");
    }

    @Test
    public void validate_invalidFieldPath_shouldReturnError() {
        Query<Player> query = new Query<>(Player.class, "stats.invalidField", new HashSet<>(List.of("213")));

        List<String> errors = query.validate();

        assertFalse(errors.isEmpty(), "Expected validation errors for invalid field path");
        assertEquals(1, errors.size(), "Expected exactly one validation error");
        assertTrue(errors.get(0).contains("invalidField"), "Expected error message to contain 'invalidField'");
    }

    @Test
    public void validate_primitiveFieldType_shouldPass() {
        Query<Player> playerQuery = new Query<>(Player.class, "stats.numFriends", new HashSet<>(List.of("213")));
        List<String> errors = playerQuery.validate();

        assertTrue(errors.isEmpty(), "Expected no validation errors for valid primitive field path");
    }

    @Test
    public void validate_nonPrimitiveFieldType_shouldReturnError() {
        Query<Player> playerQuery = new Query<>(Player.class, "stats", new HashSet<>(List.of("")));
        List<String> errors = playerQuery.validate();

        assertFalse(errors.isEmpty(), "Expected validation errors for non-primitive field type");
        assertEquals(1, errors.size(), "Expected exactly one validation error");
        assertTrue(errors.get(0).contains("stats"), "Expected error message to contain 'stats'");
    }

    @Test
    public void matches_validateNotCalled_shouldThrowException() {
        Query<Player> playerQuery = new Query<>(Player.class, "stats.numFriends", new HashSet<>(List.of("")));
        Player player = Player.builder()
                .stats(PlayerStats.builder()
                        .numFriends(10)
                        .build())
                .build();


        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            playerQuery.matches(player);
        });

        assertTrue(exception.getMessage().contains("Ensure validation is called first"),
                "Expected exception message to contain 'Ensure validation is called first'");

    }
}

