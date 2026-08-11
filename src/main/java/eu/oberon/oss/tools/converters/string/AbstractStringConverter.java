package eu.oberon.oss.tools.converters.string;

import eu.oberon.oss.tools.converters.AbstractConverter;

import java.util.function.Function;

/**
 * An abstract base class for converters that handle bidirectional conversion between a type {@code <S>} and {@link String}.
 *
 * <p>This class uses provided functional interfaces to define the conversion logic for transforming objects of type {@code <S>}
 * to strings and vice versa. It relies on the {@link AbstractConverter} base class to centralize the core conversion mechanisms.</p>
 *
 * @param <S> the type of the source object to be converted to/from {@link String}
 *
 * @author TigerLilly64
 * @see AbstractConverter
 * @see Converter
 * @since 1.0.0
 */
public class AbstractStringConverter<S> extends AbstractConverter<S, String> implements Converter<S> {

    /**
     * Constructs an {@code AbstractStringConverter} with the specified source type and conversion functions for bidirectional conversion between the source
     * type and {@link String}.
     *
     * @param sourceType     the source class type to be converted to/from {@link String}
     * @param sourceToTarget a function that converts an instance of the source type to a {@link String}
     * @param targetToSource a function that converts a {@link String} to an instance of the source type
     *
     * @since 1.0.0
     */
    protected AbstractStringConverter(Class<S> sourceType, Function<S, String> sourceToTarget, Function<String, S> targetToSource) {
        super(sourceType, String.class, sourceToTarget, targetToSource);
    }

    @Override
    public Class<S> getTypeClass() {
        return getSourceType();
    }

    @Override
    public Function<S, String> convertToString() {
        return getToTargetFunction();
    }

    @Override
    public Function<String, S> convertFromString() {
        return getToSourceFunction();
    }
}
