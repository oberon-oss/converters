package eu.oberon.oss.tools.converters.string.std;

import eu.oberon.oss.tools.converters.string.AbstractStringConverter;
import eu.oberon.oss.tools.converters.string.Converter;

/**
 * Converter for {@link Integer} type.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class IntegerConverter extends AbstractStringConverter<Integer> implements Converter<Integer> {

    /**
     * Default constructor.
     *
     * @since 1.0.0
     */
    public IntegerConverter() {
        super(Integer.class, Object::toString, Integer::parseInt);
    }
}
