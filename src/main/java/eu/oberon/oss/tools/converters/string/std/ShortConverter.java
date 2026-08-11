package eu.oberon.oss.tools.converters.string.std;

import eu.oberon.oss.tools.converters.string.AbstractStringConverter;
import eu.oberon.oss.tools.converters.string.Converter;

/**
 * Converter for {@link Short} type.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class ShortConverter extends AbstractStringConverter<Short> implements Converter<Short> {

    /**
     * Default constructor.
     *
     * @since 1.0.0
     */
    public ShortConverter() {
        super(Short.class, String::valueOf, Short::valueOf);
    }
}
