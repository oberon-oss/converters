package eu.oberon.oss.tools.converters.string.std;

import eu.oberon.oss.tools.converters.string.AbstractStringConverter;
import eu.oberon.oss.tools.converters.string.Converter;

/**
 * Converter for {@link Byte} type.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class ByteConverter extends AbstractStringConverter<Byte> implements Converter<Byte> {

    /**
     * Default constructor.
     *
     * @since 1.0.0
     */
    public ByteConverter() {
        super(Byte.class, String::valueOf, Byte::valueOf);
    }
}
