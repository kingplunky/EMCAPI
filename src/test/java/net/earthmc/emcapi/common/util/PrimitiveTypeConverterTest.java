package net.earthmc.emcapi.common.util;
import net.earthmc.emcapi.endpoints.player.object.Player;
import net.earthmc.emcapi.util.PrimitiveTypeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.NullType;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class PrimitiveTypeConverterTest {

    private PrimitiveTypeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new PrimitiveTypeConverter();
    }

    @Test
    void castStringToType_validString_shouldReturnString() {
        String result = converter.castStringToType("test", String.class);
        assertEquals("test", result);
    }

    @Test
    void castStringToType_validInteger_shouldReturnInteger() {
        Integer result = converter.castStringToType("123", Integer.class);
        assertEquals(123, result);
    }

    @Test
    void castStringToType_validBooleanTrue_shouldReturnTrue() {
        Boolean result = converter.castStringToType("true", Boolean.class);
        assertTrue(result);
    }

    @Test
    void castStringToType_validBooleanFalse_shouldReturnFalse() {
        Boolean result = converter.castStringToType("false", Boolean.class);
        assertFalse(result);
    }

    @Test
    void castStringToType_invalidBoolean_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            converter.castStringToType("notabool", Boolean.class);
        });
    }

    @Test
    void castStringToType_validDouble_shouldReturnDouble() {
        Double result = converter.castStringToType("123.45", Double.class);
        assertEquals(123.45, result);
    }

    @Test
    void castStringToType_validUUID_shouldReturnUUID() {
        UUID result = converter.castStringToType("123e4567-e89b-12d3-a456-426614174000", UUID.class);
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), result);
    }

    @Test
    void castStringToType_unsupportedType_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> {
            converter.castStringToType("test", NullType.class);
        });
    }

    @Test
    void castStringToType_validCharacter_shouldReturnCharacter() {
        Character result = converter.castStringToType("a", Character.class);
        assertEquals('a', result);
    }

    @Test
    void castStringToType_invalidCharacter_shouldReturnNull() {
        assertNull(converter.castStringToType("abc", Character.class),
                "Expected null when converting string longer than 1 to Character.");
    }

    @Test
    void isSupported_supportedType_shouldReturnTrue() {
        assertTrue(converter.isSupported(Integer.class));
    }

    @Test
    void isSupported_unsupportedType_shouldReturnFalse() {
        assertFalse(converter.isSupported(Player.class));
    }

    @Test
    void castStringToType_invalidInteger_shouldThrowException() {
        assertThrows(NumberFormatException.class, () -> {
            converter.castStringToType("abc", Integer.class);
        });
    }

    @Test
    void castStringToType_emptyString_shouldThrowException() {
        assertThrows(NumberFormatException.class, () -> {
            converter.castStringToType("", Integer.class);
        });
    }

    @Test
    void castStringToType_nullString_shouldThrowException() {
        assertThrows(NumberFormatException.class, () -> {
            converter.castStringToType(null, Integer.class);
        });
    }
}

