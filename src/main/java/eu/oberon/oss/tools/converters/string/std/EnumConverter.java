package eu.oberon.oss.tools.converters.string.std;

import eu.oberon.oss.tools.converters.string.AbstractStringConverter;
import eu.oberon.oss.tools.converters.string.Converter;

/**
 * A generic converter implementation for converting between {@link Enum} types and their {@link String} representations.
 * <p>
 * This class provides functionality to: - Convert an enum value to its string representation (name of the enum constant). - Convert a string back to the
 * corresponding enum value.
 *
 * @param <E> The type of the enum class that can be converted to and from strings.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class EnumConverter<E extends Enum<E>> extends AbstractStringConverter<E> implements Converter<E> {
    /**
     * Constructs an EnumConverter for the specified enum class.
     *
     * @param enumClass The enum class to be converted.
     *
     * @since 1.0.0
     */
    public EnumConverter(Class<E> enumClass) {
        super(enumClass, Enum::name, value -> Enum.valueOf(enumClass, value));
    }
}
