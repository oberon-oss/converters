package eu.oberon.oss.tools.converters.string.std;

import eu.oberon.oss.tools.converters.string.AbstractStringConverter;
import eu.oberon.oss.tools.converters.string.Converter;

/**
 * Converter for {@link Long} type.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class LongConverter extends AbstractStringConverter<Long> implements Converter<Long> {

    /**
     * Default constructor.
     *
     * @since 1.0.0
     */
    public LongConverter() {
        super(Long.class, String::valueOf, Long::valueOf);
    }
}
