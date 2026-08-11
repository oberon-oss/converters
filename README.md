# Build status
[![Quality gate status](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_converters&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=oberon-oss_converters)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_converters&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=oberon-oss_converters)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_converters&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=oberon-oss_converters)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_converters&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=oberon-oss_converters)

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_converters&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=oberon-oss_converters)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_converters&metric=coverage)](https://sonarcloud.io/summary/new_code?id=oberon-oss_converters)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_converters&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=oberon-oss_converters)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_converters&metric=bugs)](https://sonarcloud.io/summary/new_code?id=oberon-oss_converters)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_converters&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=oberon-oss_converters)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_converters&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=oberon-oss_converters)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_converters&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=oberon-oss_converters)

# Converters

Generic tool to provide services for converting between different class types.

The library is built around bidirectional converters: a converter describes both directions of a conversion pair. For example, a converter for
`Integer <-> String` can convert an `Integer` to a `String`, and can also convert that `String` back to an `Integer`.

## Design

The core abstraction is `BiDirectionalConverter<S, T>`.

A bidirectional converter defines:

1. the source type `S`
2. the target type `T`
3. a function from `S` to `T`
4. a function from `T` to `S`

This means a single converter represents the complete relationship between two types:

text S <-> T

Converters are managed through `BiDirectionalConvertersRegistry`.

The registry uses a strict registration policy:

- only one converter may exist for a type pair
- the direction does not matter when checking uniqueness
- registering `A <-> B` and then `B <-> A` is rejected
- registering another `A <-> B` converter is also rejected

This prevents ambiguous conversion behavior where two converters could produce different results for the same logical type pair.

For string-based conversions, the `Converter<S>` interface specializes `BiDirectionalConverter<S, String>`. This is useful for converting values to and from
their string representation.

## Standard string converters

The library provides standard converters for common Java types:

1. `Boolean`
2. `Byte`
3. `Double`
4. `Float`
5. `Integer`
6. `Long`
7. `Short`

A special case is also provided for enum classes, allowing enum values to be converted to and from their `String` names.

## Standard example: Integer conversion

```java
import eu.oberon.oss.tools.converters.ConvertersRegistry; import eu.oberon.oss.tools.converters.string.Converter;
public class IntegerConversionExample {
    public static void main(String[] args) {
        ConvertersRegistry registry = new ConvertersRegistry();


        Converter<Integer> converter = registry.getConverterForClassType(Integer.class);

        Integer value = converter.convertFromString().apply("123");
        String text = converter.convertToString().apply(value);

        System.out.println(value); // 123
        System.out.println(text);  // 123
    }
}
```

## Standard example: Enum conversion
```java 
import eu.oberon.oss.tools.converters.ConvertersRegistry; 
import eu.oberon.oss.tools.converters.string.Converter;

public class EnumConversionExample {
    enum Environment {DEVELOPMENT, TEST, PRODUCTION}

    public static void main(String[] args) {
        ConvertersRegistry registry = new ConvertersRegistry();

        Converter<Environment> converter = registry.getConverterForClassType(Environment.class);

        Environment environment = converter.convertFromString().apply("PRODUCTION");
        String text = converter.convertToString().apply(environment);

        System.out.println(environment); // PRODUCTION
        System.out.println(text);        // PRODUCTION
    }
}
```
## Custom bidirectional converter

For custom types, implement a `BiDirectionalConverter<S, T>` directly, or extend `AbstractConverter<S, T>`.

The following example converts a custom object to a tab-separated string and back again.

```java
import eu.oberon.oss.tools.converters.AbstractConverter;
import java.time.LocalDate; import java.util.UUID;

public class CustomConversionExample {
    record CustomTestClass(UUID uuid, LocalDate localDate, String description) {
    }

    static class CustomTestClassConverter extends AbstractConverter<CustomTestClass, String> {
        CustomTestClassConverter() {
            super(
                    CustomTestClass.class,
                    String.class,
                    value -> value.uuid() + "\t" + value.localDate() + "\t" + value.description(),
                    value -> {
                        String[] parts = value.split("\t", 3);

                        return new CustomTestClass(
                                UUID.fromString(parts[0]),
                                LocalDate.parse(parts[1]),
                                parts[2]);
                    });
        }
    }

    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        LocalDate localDate = LocalDate.of(2026, 8, 11);
        String description = "test";

        CustomTestClass original = new CustomTestClass(uuid, localDate, description);
        CustomTestClassConverter converter = new CustomTestClassConverter();

        String text = converter.getToTargetFunction().apply(original);
        CustomTestClass restored = converter.getToSourceFunction().apply(text);

        System.out.println(text);
        System.out.println(original.equals(restored)); // true
    }
}
```
## Registering custom converters

Custom converters can be registered in a `BiDirectionalConvertersRegistry`.

```java
import eu.oberon.oss.tools.converters.BiDirectionalConverter; 
import eu.oberon.oss.tools.converters.BiDirectionalConvertersRegistry;

public class CustomRegistryExample {
    public static void main(String[] args) {
        BiDirectionalConvertersRegistry registry = new BiDirectionalConvertersRegistry();
        CustomConversionExample.CustomTestClassConverter converter =
                new CustomConversionExample.CustomTestClassConverter();

        registry.registerConverter(converter);

        BiDirectionalConverter<CustomConversionExample.CustomTestClass, String> registeredConverter =
                registry.getConverterForClassTypes(CustomConversionExample.CustomTestClass.class, String.class);

        String text = registeredConverter.getToTargetFunction().apply(
                new CustomConversionExample.CustomTestClass(
                        java.util.UUID.randomUUID(),
                        java.time.LocalDate.of(2026, 8, 11),
                        "test"));

        System.out.println(text);
    }
}
```

Registering another converter for the same pair is rejected:

```java
import eu.oberon.oss.tools.converters.AbstractConverter; 
import eu.oberon.oss.tools.converters.BiDirectionalConvertersRegistry;

public class DuplicateConverterExample { record UserId(long value) { }
    static class UserIdToStringConverter extends AbstractConverter<UserId, String> {
        UserIdToStringConverter() {
            super(
                    UserId.class,
                    String.class,
                    userId -> Long.toString(userId.value()),
                    value -> new UserId(Long.parseLong(value)));
        }
    }

    static class AlternativeUserIdToStringConverter extends AbstractConverter<UserId, String> {
        AlternativeUserIdToStringConverter() {
            super(
                    UserId.class,
                    String.class,
                    userId -> "user-" + userId.value(),
                    value -> new UserId(Long.parseLong(value.substring("user-".length()))));
        }
    }

    static class StringToUserIdConverter extends AbstractConverter<String, UserId> {
        StringToUserIdConverter() {
            super(
                    String.class,
                    UserId.class,
                    value -> new UserId(Long.parseLong(value)),
                    userId -> Long.toString(userId.value()));
        }
    }

    public static void main(String[] args) {
        BiDirectionalConvertersRegistry registry = new BiDirectionalConvertersRegistry();

        registry.registerConverter(new UserIdToStringConverter());

        try {
            registry.registerConverter(new AlternativeUserIdToStringConverter());
        } catch (IllegalArgumentException exception) {
            System.out.println("Duplicate converter rejected");
        }

        try {
            registry.registerConverter(new StringToUserIdConverter());
        } catch (IllegalArgumentException exception) {
            System.out.println("Reverse converter rejected");
        }
    }
}
```