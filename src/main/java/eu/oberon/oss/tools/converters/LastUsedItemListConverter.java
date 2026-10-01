package eu.oberon.oss.tools.converters;

import eu.oberon.oss.tools.converters.string.AbstractStringConverter;
import eu.oberon.oss.tools.converters.string.Converter;
import eu.oberon.oss.tools.converters.util.DefaultLastUsedItemList;
import eu.oberon.oss.tools.converters.util.LastUsedItemList;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

/**
 * A converter implementation for serializing and deserializing {@link LastUsedItemList} objects to and from {@link String} using a custom {@link Converter} for
 * individual items within the list.
 *
 * <p>This class extends {@link AbstractStringConverter} to facilitate bidirectional conversion. It utilizes the Jackson {@link ObjectMapper} for JSON
 * serialization and deserialization, enabling the representation of a {@link LastUsedItemList} as a JSON array of strings.</p>
 *
 * @param <I> The type of the individual items contained in the {@link LastUsedItemList}.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class LastUsedItemListConverter<I> extends AbstractStringConverter<LastUsedItemList<I>> implements Converter<LastUsedItemList<I>> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Constructs a {@code LastUsedItemListConverter} that provides conversion between a {@link LastUsedItemList} and its {@link String} representation using a
     * specified {@link Converter} for the items within the list.
     *
     * @param itemConverter A {@link Converter} implementation that defines the conversion logic for the individual items within the {@link LastUsedItemList}.
     *                      Cannot be null.
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public LastUsedItemListConverter(Converter<I> itemConverter) {
        super(
                (Class<LastUsedItemList<I>>) (Class<?>) LastUsedItemList.class,
                lastUsedItemList -> toString(lastUsedItemList, itemConverter),
                input -> fromString(input, itemConverter)
        );
    }

    private static <I> String toString(LastUsedItemList<I> lastUsedItemList, Converter<I> itemConverter) {
        Objects.requireNonNull(lastUsedItemList, "Parameter: lastUsedItemList");
        Objects.requireNonNull(itemConverter, "Parameter: itemConverter");

        List<String> output = lastUsedItemList.getList()
                .stream()
                .map(itemConverter.convertToString())
                .toList();

        try {
            return OBJECT_MAPPER.writeValueAsString(output);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Could not convert LastUsedItemList to String", exception);
        }
    }

    private static <I> LastUsedItemList<I> fromString(String input, Converter<I> itemConverter) {
        Objects.requireNonNull(input, "Parameter: input");
        Objects.requireNonNull(itemConverter, "Parameter: itemConverter");

        try {
            List<String> strings = OBJECT_MAPPER.readValue(input, new TypeReference<>() {
            });

            List<I> items = strings.stream()
                    .map(itemConverter.convertFromString())
                    .toList();

            return new DefaultLastUsedItemList<>(items);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Could not convert String to LastUsedItemList", exception);
        }
    }
}
