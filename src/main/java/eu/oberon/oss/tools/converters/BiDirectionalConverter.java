package eu.oberon.oss.tools.converters;

import java.util.function.Function;

/**
 * Represents a bidirectional converter that can convert between two types.
 *
 * @param <S> the source type to be converted
 * @param <T> the target type to be converted
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public interface BiDirectionalConverter<S, T> {
    /**
     * Returns the source type of the converter.
     *
     * @return the source type
     *
     * @since 1.0.0
     */
    Class<S> getSourceType();

    /**
     * Returns the target type of the converter.
     *
     * @return the target type
     *
     * @since 1.0.0
     */
    Class<T> getTargetType();

    /**
     * Returns the function to convert from the source type to the target type.
     *
     * @return the function to convert from the source type to the target type
     *
     * @since 1.0.0
     */
    Function<S, T> getToTargetFunction();

    /**
     * Returns the function to convert from the target type to the source type.
     *
     * @return the function to convert from the target type to the source type
     *
     * @since 1.0.0
     */
    Function<T, S> getToSourceFunction();
}
