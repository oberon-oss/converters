package eu.oberon.oss.tools.converters.string.std;

import eu.oberon.oss.tools.converters.string.AbstractStringConverter;
import eu.oberon.oss.tools.converters.string.Converter;

/**
 * Converter for {@link Float} type.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class FloatConverter extends AbstractStringConverter<Float> implements Converter<Float> {

    /**
     * Default constructor.
     *
     * @since 1.0.0
     */
    public FloatConverter() {
        super(Float.class, String::valueOf, Float::valueOf);
    }
}
