package eu.oberon.oss.tools.converters.util;

import java.util.List;

/**
 * Represents a list of items that maintains a limited number of the most recently used items.
 *
 * @param <I> The type of items in the list.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public interface LastUsedItemList<I> {
    /**
     * Sets the maximum number of items that the list can hold. If the current size of the list exceeds the specified maximum size, the oldest items will be
     * removed to reduce the size to the specified limit.
     *
     * @param maxSize The maximum number of items allowed in the list. Must be greater than or equal to 1. Throws IllegalArgumentException if a value less than
     *                1 is provided.
     *
     * @throws IllegalArgumentException if maxSize is less than 1
     * @since 1.0.0
     */
    void setMaxSize(int maxSize);

    /**
     * Returns the current number of items in the list.
     *
     * @return The number of items currently stored in the list.
     *
     * @since 1.0.0
     */
    int currentSize();

    /**
     * Adds an item to the list, maintaining the maximum size limit. If the list is at its maximum capacity, the oldest item will be removed to make room for
     * the new item.
     *
     * @param item The item to be added to the list.
     *
     * @since 1.0.0
     */
    void add(I item);

    /**
     * Returns the list of items currently stored in the list.
     *
     * @return The list of items currently stored in the list.
     *
     * @since 1.0.0
     */
    List<I> getList();

    /**
     * Returns the maximum size of the list.
     *
     * @return The maximum number of items allowed in the list.
     *
     * @since 1.0.0
     */
    int getMaxSize();

    /**
     * Clears the list of all items.
     *
     * @since 1.0.0
     */
    void clear();
}
