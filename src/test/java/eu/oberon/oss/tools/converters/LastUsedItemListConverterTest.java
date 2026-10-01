package eu.oberon.oss.tools.converters;

import eu.oberon.oss.tools.converters.string.std.IntegerConverter;
import eu.oberon.oss.tools.converters.util.DefaultLastUsedItemList;
import eu.oberon.oss.tools.converters.util.LastUsedItemList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class LastUsedItemListConverterTest {

    private LastUsedItemListConverter<Integer> converter;

    @BeforeEach
    void setUp() {
        converter = new LastUsedItemListConverter<>(new IntegerConverter());
    }

    @Test
    void testConvertToString() {
        LastUsedItemList<Integer> list = new DefaultLastUsedItemList<>(List.of(1, 2, 3));
        String json = converter.convertToString().apply(list);
        assertEquals("[\"1\",\"2\",\"3\"]", json);
    }

    @Test
    void testConvertFromString() {
        String json = "[\"1\",\"2\",\"3\"]";
        LastUsedItemList<Integer> list = converter.convertFromString().apply(json);
        assertNotNull(list);
        assertEquals(3, list.currentSize());
        assertEquals(List.of(1, 2, 3), list.getList());
    }

    @Test
    void testGetTypeClass() {
        assertEquals(LastUsedItemList.class, converter.getTypeClass());
    }

    @Test
    void testConvertToStringWithNullThrowsException() {
        Function<LastUsedItemList<Integer>, String> foundConverter = this.converter.convertToString();
        assertNotNull(foundConverter);
        assertThrows(NullPointerException.class, () -> foundConverter.apply(null));
    }

    @Test
    void testConvertFromStringWithNullThrowsException() {
        Function<String, LastUsedItemList<Integer>> foundConverter = this.converter.convertFromString();
        assertNotNull(foundConverter);
        assertThrows(NullPointerException.class, () -> foundConverter.apply(null));
    }

    @Test
    void testConvertFromStringWithEmptyList() {
        String json = "[]";
        LastUsedItemList<Integer> list = converter.convertFromString().apply(json);
        assertNotNull(list);
        assertEquals(0, list.currentSize());
        assertEquals(0, list.getMaxSize());
    }

    @Test
    void testConvertFromStringWithInvalidJsonThrowsException() {
        Function<String, LastUsedItemList<Integer>> foundConverter = this.converter.convertFromString();
        assertNotNull(foundConverter);
        assertThrows(IllegalArgumentException.class, () -> foundConverter.apply("not a json"));
    }

    @Test
    void testClearMethod() {
        LastUsedItemList<Integer> list = new DefaultLastUsedItemList<>(List.of(1, 2, 3));
        assertEquals(3, list.currentSize());
        list.clear();
        assertEquals(0, list.currentSize());
        assertEquals(3, list.getMaxSize());
    }

}
