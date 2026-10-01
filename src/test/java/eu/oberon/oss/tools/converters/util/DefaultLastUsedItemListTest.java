package eu.oberon.oss.tools.converters.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("DuplicateExpressions")
class DefaultLastUsedItemListTest {

    @Test
    void testAddFileHonorsMaxSize() {
        DefaultLastUsedItemList<Path> list = new DefaultLastUsedItemList<>(3);

        list.add(Path.of("file1"));
        list.add(Path.of("file2"));
        list.add(Path.of("file3"));

        List<Path> files = list.getList();
        assertEquals(3, files.size(), "List should contain 3 files when maxSize is 3");
        assertEquals(Path.of("file3"), files.get(0));
        assertEquals(Path.of("file2"), files.get(1));
        assertEquals(Path.of("file1"), files.get(2));

        list.add(Path.of("file4"));
        files = list.getList();
        assertEquals(3, files.size(), "List should still contain 3 files after adding 4th");
        assertEquals(Path.of("file4"), files.get(0));
        assertEquals(Path.of("file3"), files.get(1));
        assertEquals(Path.of("file2"), files.get(2));
    }

    @Test
    void testSetMaxSizeTruncatesList() {
        DefaultLastUsedItemList<Path> list = new DefaultLastUsedItemList<>(5);

        list.add(Path.of("file1"));
        list.add(Path.of("file2"));
        list.add(Path.of("file3"));
        list.add(Path.of("file4"));
        list.add(Path.of("file5"));

        assertEquals(5, list.currentSize());

        list.setMaxSize(3);
        assertEquals(3, list.currentSize(), "List should be truncated to 3 files");
        List<Path> files = list.getList();
        assertEquals(Path.of("file5"), files.get(0));
        assertEquals(Path.of("file4"), files.get(1));
        assertEquals(Path.of("file3"), files.get(2));
    }

    @Test
    void testInvalidMaxSize() {
        DefaultLastUsedItemList<Path> list = new DefaultLastUsedItemList<>(1);
        assertThrows(IllegalArgumentException.class, () -> list.setMaxSize(0));
        assertThrows(IllegalArgumentException.class, () -> list.setMaxSize(-1));
    }

    @Test
    void testAddDuplicateMovesToFront() {
        DefaultLastUsedItemList<Path> list = new DefaultLastUsedItemList<>(3);

        list.add(Path.of("file1"));
        list.add(Path.of("file2"));
        list.add(Path.of("file1"));

        List<Path> files = list.getList();
        // If we want uniqueness:
        assertEquals(2, files.size(), "List should contain unique files");
        assertEquals(Path.of("file1"), files.get(0), "Duplicate should be moved to front");
        assertEquals(Path.of("file2"), files.get(1));
    }

    @Test
    void testConstructorWithList() {
        List<Path> paths = List.of(Path.of("file1"), Path.of("file2"));
        DefaultLastUsedItemList<Path> list = new DefaultLastUsedItemList<>(paths);

        assertEquals(2, list.currentSize());
        assertEquals(2, list.getMaxSize());
        assertEquals(Path.of("file1"), list.getList().get(0));
        assertEquals(Path.of("file2"), list.getList().get(1));
    }

    @Test
    void testConstructorWithListAndMaxSize() {
        List<Path> paths = List.of(Path.of("file1"), Path.of("file2"));
        DefaultLastUsedItemList<Path> list = new DefaultLastUsedItemList<>(paths, 5);

        assertEquals(2, list.currentSize());
        assertEquals(5, list.getMaxSize());
    }

    @Test
    void testConstructorWithListAndSmallerMaxSize() {
        List<Path> paths = List.of(Path.of("file1"), Path.of("file2"), Path.of("file3"));
        DefaultLastUsedItemList<Path> list = new DefaultLastUsedItemList<>(paths, 2);

        assertEquals(2, list.currentSize());
        assertEquals(3, list.getMaxSize(), "maxSize should be at least the size of the initial list");
    }

    @Test
    void testConstructorWithEmptyList() {
        DefaultLastUsedItemList<Path> list = new DefaultLastUsedItemList<>(List.of());
        assertEquals(0, list.currentSize());
        assertEquals(0, list.getMaxSize(), "maxSize should be 0 to reflect the size of the initial list");
    }

    @Test
    void testConstructorWithEmptyListAndMaxSize() {
        DefaultLastUsedItemList<Path> list = new DefaultLastUsedItemList<>(List.of(), 5);
        assertEquals(0, list.currentSize());
        assertEquals(5, list.getMaxSize());
    }

    @Test
    void testConstructorWithInvalidMaxSize() {
        assertThrows(IllegalArgumentException.class, () -> new DefaultLastUsedItemList<>(0));
        assertThrows(IllegalArgumentException.class, () -> new DefaultLastUsedItemList<>(-1));
    }

    @Test
    void testConstructorWithListAndInvalidMaxSize() {
        List<Path> paths = List.of(Path.of("file1"));
        assertThrows(IllegalArgumentException.class, () -> new DefaultLastUsedItemList<>(paths, 0));
        assertThrows(IllegalArgumentException.class, () -> new DefaultLastUsedItemList<>(paths, -1));
    }

    @Test
    void testConstructorWithNullListThrowsException() {
        assertThrows(NullPointerException.class, () -> new DefaultLastUsedItemList<Path>(null));
        assertThrows(NullPointerException.class, () -> new DefaultLastUsedItemList<Path>(null, 5));
    }
    @Test
    void testSetMaxSizeDoesNotTruncateIfSmaller() {
        DefaultLastUsedItemList<Path> list = new DefaultLastUsedItemList<>(5);
        list.add(Path.of("file1"));
        list.add(Path.of("file2"));

        assertEquals(2, list.currentSize());

        list.setMaxSize(3);
        assertEquals(2, list.currentSize(), "List should not be truncated as it's already smaller than new maxSize");
        assertEquals(3, list.getMaxSize());
    }

    @Test
    void testSetMaxSizeToCurrentSize() {
        DefaultLastUsedItemList<Path> list = new DefaultLastUsedItemList<>(2);
        list.add(Path.of("file1"));
        list.add(Path.of("file2"));

        assertEquals(2, list.currentSize());

        list.setMaxSize(2);
        assertEquals(2, list.currentSize());
        assertEquals(2, list.getMaxSize());
    }
}
