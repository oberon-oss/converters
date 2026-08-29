package eu.oberon.oss.tools.converters;

import eu.oberon.oss.tools.converters.string.Converter;
import eu.oberon.oss.tools.converters.string.std.IntegerConverter;
import eu.oberon.oss.tools.converters.util.LastUsedItemList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LastUsedItemListConverterFactoryTest {

    private LastUsedItemListConverterFactory factory;
    private ConvertersRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ConvertersRegistry();
        factory = new LastUsedItemListConverterFactory(registry);
    }

    @Test
    void testCreateForItemType() {
        // IntegerConverter should be registered via SPI in ConvertersRegistry
        Converter<LastUsedItemList<Integer>> converter = factory.createForItemType(Integer.class);
        assertNotNull(converter);
        assertEquals(LastUsedItemList.class, converter.getTypeClass());
    }

    @Test
    void testCreateForItemTypeThrowsWhenNoConverterRegistered() {
        assertThrows(IllegalArgumentException.class, () -> factory.createForItemType(Object.class));
    }

    @Test
    void testCreateForItemConverter() {
        IntegerConverter itemConverter = new IntegerConverter();
        Converter<LastUsedItemList<Integer>> converter = factory.createForItemConverter(itemConverter);
        assertNotNull(converter);
        assertEquals(LastUsedItemList.class, converter.getTypeClass());
    }

    @Test
    void testWithDefaultRegistry() {
        LastUsedItemListConverterFactory defaultFactory = LastUsedItemListConverterFactory.withDefaultRegistry();
        assertNotNull(defaultFactory);
        Converter<LastUsedItemList<Integer>> converter = defaultFactory.createForItemType(Integer.class);
        assertNotNull(converter);
    }

    @Test
    void testConstructorThrowsOnNullRegistry() {
        assertThrows(NullPointerException.class, () -> new LastUsedItemListConverterFactory(null));
    }
}
