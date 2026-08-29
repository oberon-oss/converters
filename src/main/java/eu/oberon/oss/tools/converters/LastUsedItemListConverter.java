package eu.oberon.oss.tools.converters;

import eu.oberon.oss.tools.converters.string.AbstractStringConverter;
import eu.oberon.oss.tools.converters.string.Converter;
import eu.oberon.oss.tools.converters.util.DefaultLastUsedItemList;
import eu.oberon.oss.tools.converters.util.LastUsedItemList;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

public class LastUsedItemListConverter<I> extends AbstractStringConverter<LastUsedItemList<I>> implements Converter<LastUsedItemList<I>> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
