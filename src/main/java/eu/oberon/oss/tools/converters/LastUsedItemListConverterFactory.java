package eu.oberon.oss.tools.converters;

import eu.oberon.oss.tools.converters.string.Converter;
import eu.oberon.oss.tools.converters.util.LastUsedItemList;

import java.util.Objects;

/**
 * Factory for creating converters for {@link LastUsedItemList} values.
 * <p>
 * These converters are created on demand instead of being registered globally, because {@code LastUsedItemList<String>}, {@code LastUsedItemList<Integer>},
 * etc. all have the same raw runtime class: {@code LastUsedItemList.class}.
 *
 * @author TigerLilly64
 * @since 1.1.0
 */
public final class LastUsedItemListConverterFactory {
    private final ConvertersRegistry convertersRegistry;

    /**
     * Creates a new factory backed by the supplied converter registry.
     *
     * @param convertersRegistry the registry used to find converters for list item types
     *
     * @throws NullPointerException if {@code convertersRegistry} is {@code null}
     * @since 1.1.0
     */
    public LastUsedItemListConverterFactory(ConvertersRegistry convertersRegistry) {
        this.convertersRegistry = Objects.requireNonNull(convertersRegistry, "Parameter: convertersRegistry");
    }

    /**
     * Creates a converter for {@code LastUsedItemList<I>} using the registered converter for {@code I}.
     *
     * @param itemType the item type stored in the last-used-item list
     * @param <I>      the item type
     *
     * @return a converter for {@code LastUsedItemList<I>}
     *
     * @throws NullPointerException     if {@code itemType} is {@code null}
     * @throws IllegalArgumentException if no converter is registered for {@code itemType}
     * @since 1.1.0
     */
    public <I> Converter<LastUsedItemList<I>> createForItemType(Class<I> itemType) {
        Objects.requireNonNull(itemType, "Parameter: itemType");

        Converter<I> itemConverter = convertersRegistry.getConverterForClassType(itemType);

        if (itemConverter == null) {
            throw new IllegalArgumentException("No converter registered for item type " + itemType.getName());
        }

        return createForItemConverter(itemConverter);
    }

    /**
     * Creates a converter for {@code LastUsedItemList<I>} using the supplied item converter.
     * <p>
     * This overload is useful when the item converter is custom or not registered.
     *
     * @param itemConverter the converter for individual list items
     * @param <I>           the item type
     *
     * @return a converter for {@code LastUsedItemList<I>}
     *
     * @throws NullPointerException if {@code itemConverter} is {@code null}
     * @since 1.1.0
     */
    public <I> Converter<LastUsedItemList<I>> createForItemConverter(Converter<I> itemConverter) {
        Objects.requireNonNull(itemConverter, "Parameter: itemConverter");

        return new LastUsedItemListConverter<>(itemConverter);
    }

    /**
     * Convenience method for creating a factory with a fresh default registry.
     *
     * @return a new factory using a new {@link ConvertersRegistry}
     *
     * @since 1.1.0
     */
    public static LastUsedItemListConverterFactory withDefaultRegistry() {
        return new LastUsedItemListConverterFactory(new ConvertersRegistry());
    }
}