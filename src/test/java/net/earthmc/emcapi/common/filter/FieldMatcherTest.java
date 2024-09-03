package net.earthmc.emcapi.common.filter;

import net.earthmc.emcapi.endpoints.nation.object.NationReference;
import net.earthmc.emcapi.endpoints.player.object.Player;
import net.earthmc.emcapi.endpoints.player.object.PlayerReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FieldMatcherTest {
    private FieldMatcher<Player> fieldMatcher;
    private FieldPathParser<Player> fieldPathParser;

    @BeforeEach
    void setUp() {
        fieldMatcher = new FieldMatcher<>();
        fieldPathParser = new FieldPathParser<>(Player.class, 5);
    }

    @Test
    void matches_fieldValueMatchesExpectedValue_shouldReturnTrue() throws NoSuchFieldException {
        Player player = Player.builder()
                .name("Fix")
                .build();

        List<Field> fields = fieldPathParser.parse("name");

        assertTrue(fieldMatcher.matches(player, fields, new HashSet<>(List.of("Fix"))),
                "Expected the match to return true when field value matches the expected value.");

    }

    @Test
    void matches_fieldValueDoesntMatchExpectedValue_shouldReturnFalse() throws NoSuchFieldException {
        Player player = Player.builder()
                .name("Fix")
                .build();

        List<Field> fields = fieldPathParser.parse("name");

        assertFalse(fieldMatcher.matches(player, fields, new HashSet<>(List.of("John"))),
                "Expected the match to return false when field value doesn't match the expected value.");

    }

    @Test
    void matches_fieldValueMatchesAtLeastOneExpectedValue_shouldReturnTrue() throws NoSuchFieldException {
        Player player = Player.builder()
                .name("Fix")
                .build();

        List<Field> fields = fieldPathParser.parse("name");

        HashSet<String> expectedValues = new HashSet<>(List.of("Largeezes", "Fix", "Player"));

        assertTrue(fieldMatcher.matches(player, fields, expectedValues),
                "Expected the match to return true when field value matches the at least one of the expected values.");

    }

    @Test
    void matches_fieldValueIsNullAndNullIsExpectedValue_shouldReturnTrue() throws NoSuchFieldException {
        Player player = Player.builder()
                .name("Fix")
                .nation(null)
                .build();

        List<Field> fields = fieldPathParser.parse("nation");

        HashSet<String> expectedValues = new HashSet<>(List.of("null"));


        assertTrue(fieldMatcher.matches(player, fields, expectedValues),
                "Expected the match to return true when field value is null and 'null' is an expected value.");
    }

    @Test
    void matches_fieldValueIsNotNullAndNullIsExpectedValue_shouldReturnFalse() throws NoSuchFieldException {
        Player player = Player.builder()
                .name("Fix")
                .nation(NationReference.builder()
                        .name("Mali")
                        .uuid(UUID.randomUUID())
                        .build())
                .build();

        List<Field> fields = fieldPathParser.parse("nation");

        HashSet<String> expectedValues = new HashSet<>(List.of("null"));


        assertFalse(fieldMatcher.matches(player, fields, expectedValues),
                "Expected the match to return false when field value is not null and 'null' is the only expected value.");
    }


    @Test
    void matches_nullInFieldPathAndNullExpectedInNestedFieldPath_shouldReturnTrue() throws NoSuchFieldException {
        Player player = Player.builder()
                .name("Fix")
                .nation(null) // nation is null, so nation.name should also be null
                .build();

        List<Field> fields = fieldPathParser.parse("nation.name");

        HashSet<String> expectedValues = new HashSet<>(List.of("null"));

        assertTrue(fieldMatcher.matches(player, fields, expectedValues),
                "Expected the match to return true when any parent of the field path is null and 'null' is an expected value.");
    }

    @Test
    void matches_listOfFieldValuesMatchExpectedValue_shouldReturnTrue() throws NoSuchFieldException {
        Player player = Player.builder()
                .friends(List.of(PlayerReference.builder()
                        .name("Largeezes")
                        .uuid(UUID.randomUUID()).build()))
                .build();

        List<Field> fields = fieldPathParser.parse("friends.name");

        HashSet<String> expectedValues = new HashSet<>(List.of("Largeezes"));

        assertTrue(fieldMatcher.matches(player, fields, expectedValues),
                "Expected the match to return true when the field path is a list of object and the expected value is within.");
    }

}
