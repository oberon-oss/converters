package eu.oberon.oss.tools.converters;

import eu.oberon.oss.tools.converters.string.Converter;
import eu.oberon.oss.tools.converters.string.std.EnumConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * A registry for managing and retrieving {@link Converter} instances.
 * <p>
 * This class provides functionality to register and look up converters for specific types. A {@link Converter} is responsible for converting objects of a
 * particular type to and from their string representation.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public final class ConvertersRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertersRegistry.class);

    private final BiDirectionalConvertersRegistry biDirectionalConvertersRegistry;

    /**
     * Constructs a new registry.
     *
     * @since 1.0.0
     */
    public ConvertersRegistry() {
        biDirectionalConvertersRegistry = new BiDirectionalConvertersRegistry();
    }

    /**
     * Retrieves a converter for a specific type.
     *
     * @param classType The class type for which to retrieve the converter.
     * @param <D>       The type of the objects that the converter handles.
     *
     * @return The converter for the specified type, or null if no converter is found.
     *
     * @since 1.0.0
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <D> @Nullable Converter<D> getConverterForClassType(Class<D> classType) {
        Converter<D> converter = (Converter<D>) biDirectionalConvertersRegistry.getConverterForClassTypes(classType, String.class);

        if (converter != null) {
            return converter;
        }

        if (classType.isEnum()) {
            LOGGER.info("Created converter for type {}", classType);
            return new EnumConverter(classType);
        }

        return null;
    }

    /**
     * Registers a converter. If a converter for the same type is already registered, it will be replaced.
     *
     * @param converter The converter to register. Must not be {@code null}.
     *
     * @since 1.0.0
     */
    public void registerConverter(@NotNull Converter<?> converter) {
        Objects.requireNonNull(converter, "Parameter: converter");
        biDirectionalConvertersRegistry.registerConverter(converter);
    }
}