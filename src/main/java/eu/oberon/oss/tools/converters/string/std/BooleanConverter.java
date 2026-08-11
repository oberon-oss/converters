package eu.oberon.oss.tools.converters.string.std;

import eu.oberon.oss.tools.converters.string.AbstractStringConverter;
import eu.oberon.oss.tools.converters.string.Converter;

/**
 * Converter for {@code Boolean} values.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class BooleanConverter extends AbstractStringConverter<Boolean> implements Converter<Boolean> {
    /**
     * Default constructor.
     *
     * @since 1.0.0
     */
    public BooleanConverter() {
        super(Boolean.class, String::valueOf, Boolean::valueOf);
    }
}
