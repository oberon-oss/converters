package eu.oberon.oss.tools.converters;

import java.util.function.Function;

/**
 * An abstract base class that implements a bidirectional converter, enabling conversions between two types {@code <S>} (source type) and {@code <T>} (target
 * type).
 * <p>
 * This class utilizes functional interfaces to define the mapping logic for both directions of conversion between the specified types. It acts as a foundation
 * for creating concrete converters by encapsulating the conversion logic and type information.
 *
 * @param <S> the source type to be converted
 * @param <T> the target type to be converted
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public abstract class AbstractConverter<S, T> implements BiDirectionalConverter<S, T> {

    private final Class<S> sourceType;
    private final Class<T> targetType;
    private final Function<S, T> sourceToTarget;
    private final Function<T, S> targetToSource;

    /**
     * Constructs an AbstractConverter instance with the specified source and target types, along with the conversion functions.
     *
     * @param sourceType     the source type to be converted
     * @param targetType     the target type to be converted
     * @param sourceToTarget the function to convert from the source type to the target type
     * @param targetToSource the function to convert from the target type to the source type
     *
     * @since 1.0.0
     */
    protected AbstractConverter(Class<S> sourceType, Class<T> targetType, Function<S, T> sourceToTarget, Function<T, S> targetToSource) {
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.sourceToTarget = sourceToTarget;
        this.targetToSource = targetToSource;
    }

    /**
     * Creates a concrete bidirectional converter with the specified source and target types and conversion functions.
     *
     * @param sourceType     the source type to be converted
     * @param targetType     the target type to be converted
     * @param sourceToTarget the function to convert from the source type to the target type
     * @param targetToSource the function to convert from the target type to the source type
     * @param <S>            the source type
     * @param <T>            the target type
     *
     * @return a concrete bidirectional converter
     *
     * @since 1.1.0
     */
    public static <S, T> BiDirectionalConverter<S, T> of(
            Class<S> sourceType,
            Class<T> targetType,
            Function<S, T> sourceToTarget,
            Function<T, S> targetToSource) {
        return new AbstractConverter<>(sourceType, targetType, sourceToTarget, targetToSource) {
        };
    }

    @Override
    public Class<S> getSourceType() {
        return sourceType;
    }

    @Override
    public Class<T> getTargetType() {
        return targetType;
    }

    @Override
    public Function<S, T> getToTargetFunction() {
        return sourceToTarget;
    }

    @Override
    public Function<T, S> getToSourceFunction() {
        return targetToSource;
    }
}
