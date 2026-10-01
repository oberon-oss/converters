package eu.oberon.oss.tools.converters.util;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a list of items that maintains a limited number of the most recently used items.
 * <p>
 * This class provides functionality to add items, enforce a maximum size limit, and retrieve the list of recently used items.
 *
 * @param <I> The type of items stored in the list.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public final class DefaultLastUsedItemList<I> implements LastUsedItemList<I> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultLastUsedItemList.class);

    private final List<I> itemList;

    @Getter
    private int maxSize;

    /**
     * Constructs a new DefaultLastUsedItemList with a specified maximum size.
     *
     * @param maxSize The maximum number of items allowed in the list. Must be greater than or equal to 1.
     *
     * @throws IllegalArgumentException if {@code maxSize} is less than 1.
     * @since 1.0.0
     */
    public DefaultLastUsedItemList(int maxSize) {
        if (maxSize < 1) {
            throw new IllegalArgumentException("Max size must be >= 1");
        }
        itemList = new LinkedList<>();
        this.maxSize = maxSize;
    }


    /**
     * Constructs a new DefaultLastUsedItemList with a predefined list of items.
     * <p>
     * The maximum size of the list will be set to the number of provided items, or 1 if the list is empty.
     *
     * @param itemList The list of initial items to populate the list with. Must not be null.
     *
     * @throws NullPointerException if the parameter {@code items} is null.
     * @since 1.0.0
     */
    public DefaultLastUsedItemList(List<I> itemList) {
        Objects.requireNonNull(itemList, "Parameter: items");
        this.itemList = new LinkedList<>(itemList);
        this.maxSize = itemList.size();

    }

    /**
     * Constructs a new DefaultLastUsedItemList with a predefined list of items and a maximum size.
     * <p>
     * The maximum size of the list will be set to the number of provided items, unless the provided maximum size is larger.
     * <p>
     * If the specified maximum size is less than the number of provided items, the list will be truncated to the specified maximum size.
     *
     * @param itemList The list of initial items to populate the list with. Must not be null.
     * @param maxSize  The maximum size of the list. Must be greater than or equal to 1.
     *
     * @throws NullPointerException     if the parameter {@code items} is null.
     * @throws IllegalArgumentException if {@code maxSize} is less than 1.
     */
    public DefaultLastUsedItemList(final List<I> itemList, int maxSize) {
        Objects.requireNonNull(itemList, "Parameter: items");
        if (maxSize < 1) {
            throw new IllegalArgumentException("Max size must be >= 1");
        }

        this.itemList = new LinkedList<>(itemList);

        this.maxSize = Math.max(this.itemList.size(), maxSize);
        while (this.itemList.size() > maxSize) {
            this.itemList.removeLast();
        }
    }

    @Override
    public void setMaxSize(int maxSize) {
        if (maxSize < 1) {
            throw new IllegalArgumentException("Max size must be >= 1");
        }
        this.maxSize = maxSize;
        if (itemList.size() > maxSize) {
            itemList.subList(maxSize, itemList.size()).clear();
        }
    }

    @Override
    public int currentSize() {
        return itemList.size();
    }


    @Override
    public void add(I item) {
        if (itemList.remove(item)) {
            LOGGER.info("Item {} already exists, moved to the top of the list.", item);
        }
        itemList.addFirst(item);
        if (itemList.size() > maxSize) {
            itemList.removeLast();
        }
    }

    /**
     * {@inheritDoc} The returned list is a copy of the internal list and is not modifiable.
     */
    @Override
    public List<I> getList() {
        return List.copyOf(itemList);
    }

    @Override
    public void clear() {
        itemList.clear();
    }
}