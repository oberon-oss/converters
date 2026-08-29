package eu.oberon.oss.tools.converters;

import eu.oberon.oss.tools.ConfigTestEnum;
import eu.oberon.oss.tools.converters.string.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ConvertersRegistryTest {
    ConvertersRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ConvertersRegistry();
        assertNotNull(registry);
    }

    public static Stream<Arguments> testConvertersRegistry() {
        return Stream.of(
                Arguments.of(Integer.class, "123", 123),
                Arguments.of(Long.class, "456", 456L),
                Arguments.of(Byte.class, "127", (byte) 127),
                Arguments.of(Short.class, "32767", (short) 32767),
                Arguments.of(Boolean.class, "true", true),
                Arguments.of(Float.class, "1.2345", (float) 1.2345),
                Arguments.of(Double.class, "56.789", 56.789),
                Arguments.of(ConfigTestEnum.class, "VALUE_C", ConfigTestEnum.VALUE_C)
        );
    }

    @Test
    void testRegisterConverterRejectsDuplicateConverter() {
        Converter<Integer> newConverter = new Converter<>() {
            @Override
            public Class<Integer> getTypeClass() {
                return Integer.class;
            }

            @Override
            public Function<Integer, String> convertToString() {
                return integer -> integer == Integer.MAX_VALUE ? "test-converter" : null;
            }

            @Override
            public Function<String, Integer> convertFromString() {
                return string -> string.contentEquals("test-converter") ? Integer.MAX_VALUE : 0;
            }
        };

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> registry.registerConverter(newConverter));

        assertTrue(exception.getMessage().contains(Integer.class.getName()));
        assertTrue(exception.getMessage().contains(String.class.getName()));
        assertTrue(exception.getMessage().contains("already registered"));
    }

    @Test
    void testEnumConverterIsRegisteredAfterFirstLookup() {
        Converter<ConfigTestEnum> firstConverter = assertDoesNotThrow(() -> registry.getConverterForClassType(ConfigTestEnum.class));
        Converter<ConfigTestEnum> secondConverter = assertDoesNotThrow(() -> registry.getConverterForClassType(ConfigTestEnum.class));

        assertNotNull(firstConverter);
        assertSame(firstConverter, secondConverter);
    }

    @Test
    void testGetLastUsedItemListConverterFactory() {
        assertNotNull(registry.getLastUsedItemListConverterFactory());
    }

    @ParameterizedTest
    @MethodSource()
    void testConvertersRegistry(Class<?> classType, String value, Object expected) {
        testConvertersRegistryTyped(classType, value, expected);
    }

    private <D> void testConvertersRegistryTyped(Class<D> classType, String value, Object expected) {
        Converter<D> converter = assertDoesNotThrow(() -> registry.getConverterForClassType(classType));
        assertNotNull(converter);

        assertEquals(converter.getTypeClass(), classType);
        D result = assertDoesNotThrow(() -> converter.convertFromString().apply(value));
        assertEquals(classType.cast(expected), result);

        assertEquals(value, converter.convertToString().apply(result));
    }
}