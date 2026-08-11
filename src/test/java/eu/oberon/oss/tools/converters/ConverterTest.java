package eu.oberon.oss.tools.converters;

import eu.oberon.oss.tools.ConfigTestEnum;
import eu.oberon.oss.tools.converters.string.Converter;
import eu.oberon.oss.tools.converters.string.std.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {

    @Test
    void testIntegerConverter() {
        IntegerConverter converter = new IntegerConverter();
        String result = converter.convertToString().apply(123);
        assertEquals("123", result);
        assertEquals(String.class, converter.getTargetType());
        int value = converter.convertFromString().apply(result);
        assertEquals(123, value);
    }

    @Test
    void testLongConverter() {
        LongConverter converter = new LongConverter();
        String result = converter.convertToString().apply(123L);
        assertEquals("123", result);
        assertEquals(String.class, converter.getTargetType());
        long value = converter.convertFromString().apply(result);
        assertEquals(123L, value);
    }

    @Test
    void testBooleanConverter() {
        BooleanConverter converter = new BooleanConverter();
        String result = converter.convertToString().apply(true);
        assertEquals("true", result);
        assertEquals(String.class, converter.getTargetType());
        boolean value = converter.convertFromString().apply(result);
        assertTrue(value);
    }

    @Test
    void testByteConverter() {
        ByteConverter converter = new ByteConverter();
        String result = converter.convertToString().apply((byte) 123);
        assertEquals("123", result);
        assertEquals(String.class, converter.getTargetType());
        byte value = converter.convertFromString().apply(result);
        assertEquals((byte) 123, value);
    }

    @Test
    void testShortConverter() {
        ShortConverter converter = new ShortConverter();
        String result = converter.convertToString().apply((short) 123);
        assertEquals("123", result);

        assertEquals(String.class, converter.getTargetType());

        short value = converter.convertFromString().apply(result);
        assertEquals((short) 123, value);
    }

    @Test
    void testFloatConverter() {
        FloatConverter converter = new FloatConverter();
        String result = converter.convertToString().apply(123.45f);
        assertEquals("123.45", result);
        assertEquals(String.class, converter.getTargetType());
        float value = converter.convertFromString().apply(result);
        assertEquals(123.45f, value);
    }

    @Test
    void testDoubleConverter() {
        DoubleConverter converter = new DoubleConverter();
        String result = converter.convertToString().apply(123.456);
        assertEquals("123.456", result);
        assertEquals(String.class, converter.getTargetType());
        double value = converter.convertFromString().apply(result);
        assertEquals(123.456, value);
    }

    @Test
    void testEnumConverter() {
        EnumConverter<ConfigTestEnum> converter = new EnumConverter<>(ConfigTestEnum.class);
        String result = converter.convertToString().apply(ConfigTestEnum.VALUE_A);
        assertEquals("VALUE_A", result);
        assertEquals(String.class, converter.getTargetType());
        ConfigTestEnum value = converter.convertFromString().apply(result);
        assertEquals(ConfigTestEnum.VALUE_A, value);
    }

    @Test
    void testConverterDefaultGetToTargetFunctionDelegatesToConvertToString() {
        Converter<Integer> converter = new Converter<>() {
            private final Function<Integer, String> toStringFunction = value -> "value-" + value;
            private final Function<String, Integer> fromStringFunction = value -> Integer.valueOf(value.substring("value-".length()));

            @Override
            public Class<Integer> getTypeClass() {
                return Integer.class;
            }

            @Override
            public Function<Integer, String> convertToString() {
                return toStringFunction;
            }

            @Override
            public Function<String, Integer> convertFromString() {
                return fromStringFunction;
            }
        };

        assertSame(converter.convertToString(), converter.getToTargetFunction());
        assertEquals("value-123", converter.getToTargetFunction().apply(123));
    }

    @Test
    void testConverterDefaultGetToSourceFunctionDelegatesToConvertFromString() {
        Converter<Integer> converter = new Converter<>() {
            private final Function<Integer, String> toStringFunction = value -> "value-" + value;
            private final Function<String, Integer> fromStringFunction = value -> Integer.valueOf(value.substring("value-".length()));

            @Override
            public Class<Integer> getTypeClass() {
                return Integer.class;
            }

            @Override
            public Function<Integer, String> convertToString() {
                return toStringFunction;
            }

            @Override
            public Function<String, Integer> convertFromString() {
                return fromStringFunction;
            }
        };

        assertSame(converter.convertFromString(), converter.getToSourceFunction());
        assertEquals(123, converter.getToSourceFunction().apply("value-123"));
    }

    @Test
    void testConverterCanBeUsedAsBiDirectionalConverter() {
        BiDirectionalConverter<Integer, String> converter = new Converter<>() {
            private final Function<Integer, String> toStringFunction = value -> "value-" + value;
            private final Function<String, Integer> fromStringFunction = value -> Integer.valueOf(value.substring("value-".length()));

            @Override
            public Class<Integer> getTypeClass() {
                return Integer.class;
            }

            @Override
            public Function<Integer, String> convertToString() {
                return toStringFunction;
            }

            @Override
            public Function<String, Integer> convertFromString() {
                return fromStringFunction;
            }
        };

        assertEquals(Integer.class, converter.getSourceType());
        assertEquals(String.class, converter.getTargetType());
        assertEquals("value-123", converter.getToTargetFunction().apply(123));
        assertEquals(123, converter.getToSourceFunction().apply("value-123"));
    }

    @Test
    void testBiDirectionalConverterDirectly() {
        UUID uuid = UUID.randomUUID();
        LocalDate localDate = LocalDate.of(2026, Month.AUGUST, 11);
        String description = "test";

        String expectedString = uuid.toString() + "\t" + localDate.toString() + "\t" + description;

        CustomTestClass customTestClass = new CustomTestClass(uuid, localDate, description);
        CustomTestClassConverter converter = new CustomTestClassConverter();

        String result = converter.getToTargetFunction().apply(customTestClass);
        assertEquals(expectedString, result);

        CustomTestClass resultClass = converter.getToSourceFunction().apply(result);
        assertEquals(customTestClass, resultClass);
    }

    private static class CustomTestClassConverter extends AbstractConverter<CustomTestClass, String> {
        private CustomTestClassConverter() {
            super(CustomTestClass.class, String.class, CustomTestClass.TO_STORAGE_TYPE, CustomTestClass.TO_DATA_TYPE);
        }
    }

    private record CustomTestClass(UUID id, LocalDate inceptionDate, String description) {
            private static final String FIELD_SEPARATOR = "\t";

        private static final Function<CustomTestClass, String> TO_STORAGE_TYPE = customConfigItem -> {
                if (customConfigItem == null) {
                    return null;
                }
                return customConfigItem.id + FIELD_SEPARATOR + customConfigItem.inceptionDate + FIELD_SEPARATOR + customConfigItem.description;
            };

            private static final Function<String, CustomTestClass> TO_DATA_TYPE = string -> {
                if (string == null) {
                    return null;
                }
                String[] strings = string.split(FIELD_SEPARATOR);
                UUID newId = UUID.fromString(strings[0]);
                LocalDate newDate = LocalDate.parse(strings[1]);
                return new CustomTestClass(newId, newDate, strings[2]);
            };
        }
}