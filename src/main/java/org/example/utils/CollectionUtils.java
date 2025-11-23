package org.example.utils;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Generic utility class for common collection operations.
 * Demonstrates PECS wildcards, bounded type parameters, and lambda expressions.
 */
public final class CollectionUtils {

    private CollectionUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Filters elements based on a predicate.
     * Uses PECS: ? extends T (producer - reading from source)
     */
    public static <T> List<T> filter(
            Collection<? extends T> source,
            Predicate<? super T> predicate) {

        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        return source.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Transforms elements using a mapping function.
     * Uses PECS for both input and output.
     */
    public static <T, R> List<R> map(
            Collection<? extends T> source,
            Function<? super T, ? extends R> mapper) {

        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(mapper, "Mapper cannot be null");

        return source.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * Finds maximum element where T must be Comparable.
     * Demonstrates bounded type parameter.
     */
    public static <T extends Comparable<T>> Optional<T> findMax(
            Collection<T> elements) {

        Objects.requireNonNull(elements, "Elements cannot be null");

        return elements.stream()
                .max(Comparator.naturalOrder());
    }

    /**
     * Copies filtered elements from source to destination.
     * Uses PECS: ? extends T (producer), ? super T (consumer)
     */
    public static <T> void copyFiltered(
            Collection<? extends T> source,
            Collection<? super T> destination,
            Predicate<? super T> predicate) {

        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(destination, "Destination cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        source.stream()
                .filter(predicate)
                .forEach(destination::add);
    }

    /**
     * Counts elements matching a predicate.
     */
    public static <T> long count(
            Collection<T> source,
            Predicate<T> predicate) {

        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        return source.stream()
                .filter(predicate)
                .count();
    }

    /**
     * Groups elements by a classifier function.
     */
    public static <T, K> Map<K, List<T>> groupBy(
            Collection<? extends T> source,
            Function<? super T, ? extends K> classifier) {

        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(classifier, "Classifier cannot be null");

        return source.stream()
                .collect(Collectors.groupingBy(classifier));
    }
}
