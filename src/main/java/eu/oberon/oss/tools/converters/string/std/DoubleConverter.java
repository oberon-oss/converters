package eu.oberon.oss.tools.converters.string.std;

import eu.oberon.oss.tools.converters.string.AbstractStringConverter;
import eu.oberon.oss.tools.converters.string.Converter;

/**
 * Converter for {@link Double} type.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class DoubleConverter extends AbstractStringConverter<Double> implements Converter<Double> {
    /**
     * Default constructor.
     *
     * @since 1.0.0
     */
    public DoubleConverter() {
        super(Double.class, String::valueOf, Double::valueOf);
    }
}
