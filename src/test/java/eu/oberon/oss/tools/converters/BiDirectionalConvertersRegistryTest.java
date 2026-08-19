package eu.oberon.oss.tools.converters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BiDirectionalConvertersRegistryTest {

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRegisterConverter() {
        BiDirectionalConvertersRegistry registry = new BiDirectionalConvertersRegistry();

        BiDirectionalConverter<TestSource, TestTarget> converter = new TestSourceToTargetConverter();

        assertDoesNotThrow(() -> registry.registerConverter(converter));

        BiDirectionalConverter<TestSource, TestTarget> registeredConverter = registry.getConverterForClassTypes(TestSource.class, TestTarget.class);

        assertSame(converter, registeredConverter);
        assertEquals(new TestTarget("value-123"), registeredConverter.getToTargetFunction().apply(new TestSource(123)));
        assertEquals(new TestSource(123), registeredConverter.getToSourceFunction().apply(new TestTarget("value-123")));
    }

    @Test
    void testGetConverterForClassTypesReturnsNullWhenConverterIsNotRegistered() {
        BiDirectionalConvertersRegistry registry = new BiDirectionalConvertersRegistry();

        BiDirectionalConverter<TestSource, TestTarget> converter = registry.getConverterForClassTypes(TestSource.class, TestTarget.class);

        assertNull(converter);
    }

    @Test
    void testRegisterConverterRejectsNullConverter() {
        BiDirectionalConvertersRegistry registry = new BiDirectionalConvertersRegistry();

        NullPointerException exception = assertThrows(NullPointerException.class, () -> registry.registerConverter(null));

        assertEquals("Parameter: converter", exception.getMessage());
    }

    @Test
    void testGetConverterForClassTypesRejectsNullSourceType() {
        BiDirectionalConvertersRegistry registry = new BiDirectionalConvertersRegistry();

        NullPointerException exception = assertThrows(NullPointerException.class, () -> registry.getConverterForClassTypes(null, TestTarget.class));

        assertEquals("Parameter: sourceType", exception.getMessage());
    }

    @Test
    void testGetConverterForClassTypesRejectsNullTargetType() {
        BiDirectionalConvertersRegistry registry = new BiDirectionalConvertersRegistry();

        NullPointerException exception = assertThrows(NullPointerException.class, () -> registry.getConverterForClassTypes(TestSource.class, null));

        assertEquals("Parameter: targetType", exception.getMessage());
    }

    @Test
    void testRegisterConverterRejectsDuplicateConverterForSameDirection() {
        BiDirectionalConvertersRegistry registry = new BiDirectionalConvertersRegistry();

        registry.registerConverter(new TestSourceToTargetConverter());

        AlternativeTestSourceToTargetConverter converter = new AlternativeTestSourceToTargetConverter();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> registry.registerConverter(converter));

        assertTrue(exception.getMessage().contains(TestSource.class.getName()));
        assertTrue(exception.getMessage().contains(TestTarget.class.getName()));
        assertTrue(exception.getMessage().contains("already registered"));
    }

    @Test
    void testRegisterConverterRejectsDuplicateConverterForReverseDirection() {
        BiDirectionalConvertersRegistry registry = new BiDirectionalConvertersRegistry();

        registry.registerConverter(new TestSourceToTargetConverter());

        TestTargetToSourceConverter converter = new TestTargetToSourceConverter();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> registry.registerConverter(converter));

        assertTrue(exception.getMessage().contains(TestSource.class.getName()));
        assertTrue(exception.getMessage().contains(TestTarget.class.getName()));
        assertTrue(exception.getMessage().contains("conflicts with already registered converter"));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRejectedDuplicateDoesNotReplaceExistingConverter() {
        BiDirectionalConvertersRegistry registry = new BiDirectionalConvertersRegistry();

        BiDirectionalConverter<TestSource, TestTarget> originalConverter = new TestSourceToTargetConverter();

        registry.registerConverter(originalConverter);

        AlternativeTestSourceToTargetConverter converter = new AlternativeTestSourceToTargetConverter();
        assertThrows(IllegalArgumentException.class, () -> registry.registerConverter(converter));

        BiDirectionalConverter<TestSource, TestTarget> registeredConverter = registry.getConverterForClassTypes(TestSource.class, TestTarget.class);

        assertSame(originalConverter, registeredConverter);
        assertEquals(new TestTarget("value-123"), registeredConverter.getToTargetFunction().apply(new TestSource(123)));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRejectedReverseDuplicateDoesNotReplaceExistingConverter() {
        BiDirectionalConvertersRegistry registry = new BiDirectionalConvertersRegistry();

        BiDirectionalConverter<TestSource, TestTarget> originalConverter = new TestSourceToTargetConverter();

        registry.registerConverter(originalConverter);

        TestTargetToSourceConverter converter = new TestTargetToSourceConverter();
        assertNotNull(converter);
        assertThrows(IllegalArgumentException.class, () -> registry.registerConverter(converter));

        BiDirectionalConverter<TestSource, TestTarget> registeredConverter = registry.getConverterForClassTypes(TestSource.class, TestTarget.class);

        assertSame(originalConverter, registeredConverter);
        assertEquals(new TestTarget("value-123"), registeredConverter.getToTargetFunction().apply(new TestSource(123)));

        BiDirectionalConverter<TestTarget, TestSource> reverseRegisteredConverter = registry.getConverterForClassTypes(TestTarget.class, TestSource.class);

        assertNotNull(reverseRegisteredConverter);
        assertNotSame(converter, reverseRegisteredConverter);
        assertEquals(TestTarget.class, reverseRegisteredConverter.getSourceType());
        assertEquals(TestSource.class, reverseRegisteredConverter.getTargetType());
        assertEquals(new TestSource(123), reverseRegisteredConverter.getToTargetFunction().apply(new TestTarget("value-123")));
        assertEquals(new TestTarget("value-123"), reverseRegisteredConverter.getToSourceFunction().apply(new TestSource(123)));
    }

    private record TestSource(int value) {
    }

    private record TestTarget(String value) {
    }

    private static class TestSourceToTargetConverter extends AbstractConverter<TestSource, TestTarget> {
        private TestSourceToTargetConverter() {
            super(TestSource.class, TestTarget.class, source -> new TestTarget("value-" + source.value()), target -> new TestSource(Integer.parseInt(target.value().substring("value-".length()))));
        }
    }

    private static class AlternativeTestSourceToTargetConverter extends AbstractConverter<TestSource, TestTarget> {
        private AlternativeTestSourceToTargetConverter() {
            super(TestSource.class, TestTarget.class, source -> new TestTarget("alternative-" + source.value()), target -> new TestSource(Integer.parseInt(target.value().substring("alternative-".length()))));
        }
    }

    private static class TestTargetToSourceConverter extends AbstractConverter<TestTarget, TestSource> {
        private TestTargetToSourceConverter() {
            super(TestTarget.class, TestSource.class, target -> new TestSource(Integer.parseInt(target.value().substring("reverse-".length()))), source -> new TestTarget("reverse-" + source.value()));
        }
    }
}