package eu.oberon.oss.tools.converters;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class AbstractConverterTest {

    @Test
    void testOfStaticFactory() {
        Class<Integer> sourceType = Integer.class;
        Class<String> targetType = String.class;
        Function<Integer, String> sourceToTarget = Object::toString;
        Function<String, Integer> targetToSource = Integer::valueOf;

        BiDirectionalConverter<Integer, String> converter = AbstractConverter.of(sourceType, targetType, sourceToTarget, targetToSource);

        assertNotNull(converter);
        assertEquals(sourceType, converter.getSourceType());
        assertEquals(targetType, converter.getTargetType());
        assertEquals("123", converter.getToTargetFunction().apply(123));
        assertEquals(123, converter.getToSourceFunction().apply("123"));
    }
}
