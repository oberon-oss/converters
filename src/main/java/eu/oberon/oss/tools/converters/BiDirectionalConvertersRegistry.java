package eu.oberon.oss.tools.converters;

import eu.oberon.oss.tools.converters.string.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * A registry for managing and retrieving {@link BiDirectionalConverter} instances.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public final class BiDirectionalConvertersRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiDirectionalConvertersRegistry.class);

    private final Set<BiDirectionalConverter<?, ?>> converters;

    /**
     * Constructs a new registry.
     *
     * @since 1.0.0
     */
    public BiDirectionalConvertersRegistry() {
        converters = new HashSet<>();

        //noinspection rawtypes
        ServiceLoader<BiDirectionalConverter> loader = ServiceLoader.load(BiDirectionalConverter.class, BiDirectionalConvertersRegistry.class.getClassLoader());

        for (BiDirectionalConverter<?, ?> converter : loader) {
            registerConverter(converter);
        }


        //noinspection rawtypes
        ServiceLoader<Converter> stringConverterLoader = ServiceLoader.load(
                Converter.class,
                BiDirectionalConvertersRegistry.class.getClassLoader());

        for (Converter<?> converter : stringConverterLoader) {
            registerConverter(converter);
        }
    }

    /**
     * Retrieves a converter for a specific source and target type.
     *
     * @param sourceType the source type
     * @param targetType the target type
     * @param <S>        the source type
     * @param <T>        the target type
     *
     * @return the converter for the specified source and target types, or {@code null} if none is found
     *
     * @throws NullPointerException if either sourceType or targetType is null
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <S, T> @Nullable BiDirectionalConverter<S, T> getConverterForClassTypes(
            @NotNull Class<S> sourceType,
            @NotNull Class<T> targetType) {
        Objects.requireNonNull(sourceType, "Parameter: sourceType");
        Objects.requireNonNull(targetType, "Parameter: targetType");

        return (BiDirectionalConverter<S, T>) converters.stream()
                .filter(registeredConverter -> registeredConverter.getSourceType().equals(sourceType))
                .filter(registeredConverter -> registeredConverter.getTargetType().equals(targetType))
                .findFirst()
                .orElse(null);
    }

    /**
     * Registers a bidirectional converter. If a converter for the same source and target type is already registered, it will be replaced.
     *
     * @param converter the converter to register
     *
     * @throws NullPointerException if 'converter' is null
     * @since 1.0.0
     */
    public void registerConverter(@NotNull BiDirectionalConverter<?, ?> converter) {
        Objects.requireNonNull(converter, "Parameter: converter");

        BiDirectionalConverter<?, ?> existingConverter = getConverterForClassTypes(converter.getSourceType(), converter.getTargetType());

        if (existingConverter != null) {
            LOGGER.info(
                    "Replacing already registered converter for source type {} and target type {}",
                    converter.getSourceType(),
                    converter.getTargetType());
            converters.remove(existingConverter);
        }

        converters.add(converter);
    }
}