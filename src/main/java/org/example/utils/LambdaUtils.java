package org.example.utils;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generic utility class demonstrating lambda expressions, functional interfaces, and PECS wildcards.
 * This class provides reusable methods for common collection operations.
 */
public final class LambdaUtils {

    // Private constructor - utility class should not be instantiated
    private LambdaUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Applies a function to each element and collects results.
     * Demonstrates Function<T, R> functional interface.
     */
    public static <T, R> List<R> transform(Collection<T> source, Function<T, R> mapper) {
        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(mapper, "Mapper function cannot be null");

        return source.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * Filters elements based on a predicate.
     * Demonstrates Predicate<T> functional interface.
     */
    public static <T> List<T> filter(Collection<T> source, Predicate<T> predicate) {
        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        return source.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Performs an action on each element.
     * Demonstrates Consumer<T> functional interface.
     */
    public static <T> void forEach(Collection<T> source, Consumer<T> action) {
        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(action, "Action cannot be null");

        source.forEach(action);
    }

    /**
     * Generates a list of elements using a supplier.
     * Demonstrates Supplier<T> functional interface.
     */
    public static <T> List<T> generate(int count, Supplier<T> supplier) {
        if (count < 0) throw new IllegalArgumentException("Count must be non-negative");
        Objects.requireNonNull(supplier, "Supplier cannot be null");

        return Stream.generate(supplier)
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Combines two values using a binary operator.
     * Demonstrates BinaryOperator<T> functional interface.
     */
    public static <T> Optional<T> reduce(Collection<T> source, BinaryOperator<T> accumulator) {
        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(accumulator, "Accumulator cannot be null");

        return source.stream().reduce(accumulator);
    }

    /**
     * Generic filter with upper bounded wildcard (Producer Extends).
     * Uses '? extends T' because we're READING from the collection.
     */
    public static <T> List<T> filterWithPECS(
            Collection<? extends T> source,
            Predicate<? super T> predicate) {

        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        return source.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Generic map/transform with PECS.
     * Input uses 'extends' (reading), output uses 'extends' (producing).
     */
    public static <T, R> List<R> mapWithPECS(
            Collection<? extends T> source,
            Function<? super T, ? extends R> mapper) {

        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(mapper, "Mapper cannot be null");

        return source.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * Generic grouping with PECS.
     */
    public static <T, K> Map<K, List<T>> groupByWithPECS(
            Collection<? extends T> source,
            Function<? super T, ? extends K> classifier) {

        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(classifier, "Classifier cannot be null");

        return source.stream()
                .collect(Collectors.groupingBy(classifier));
    }

    /**
     * Copies all elements from source to destination (Consumer Super).
     * Destination uses '? super T' because we're WRITING to it.
     */
    public static <T> void copyAll(
            Collection<? extends T> source,
            Collection<? super T> destination) {

        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(destination, "Destination collection cannot be null");

        destination.addAll(source);
    }

    /**
     * Adds elements matching a predicate to destination.
     * Demonstrates both Producer (extends) and Consumer (super).
     */
    public static <T> void copyIf(
            Collection<? extends T> source,
            Collection<? super T> destination,
            Predicate<? super T> predicate) {

        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(destination, "Destination collection cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        source.stream()
                .filter(predicate)
                .forEach(destination::add);
    }

    /**
     * Finds maximum element where T must be Comparable.
     * Demonstrates single bound.
     */
    public static <T extends Comparable<T>> Optional<T> findMax(Collection<T> elements) {
        Objects.requireNonNull(elements, "Elements collection cannot be null");

        return elements.stream()
                .max(Comparator.naturalOrder());
    }

    /**
     * Finds minimum element where T must be Comparable.
     */
    public static <T extends Comparable<T>> Optional<T> findMin(Collection<T> elements) {
        Objects.requireNonNull(elements, "Elements collection cannot be null");

        return elements.stream()
                .min(Comparator.naturalOrder());
    }

    /**
     * Sorts collection where T must be Comparable.
     * Returns immutable list.
     */
    public static <T extends Comparable<T>> List<T> sortAscending(Collection<? extends T> elements) {
        Objects.requireNonNull(elements, "Elements collection cannot be null");

        return elements.stream()
                .sorted()
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Sorts collection in descending order.
     */
    public static <T extends Comparable<T>> List<T> sortDescending(Collection<? extends T> elements) {
        Objects.requireNonNull(elements, "Elements collection cannot be null");

        return elements.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Partitions collection into two groups based on predicate.
     */
    public static <T> Map<Boolean, List<T>> partition(
            Collection<T> source,
            Predicate<T> predicate) {

        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        return source.stream()
                .collect(Collectors.partitioningBy(predicate));
    }

    /**
     * Checks if any element matches the predicate.
     */
    public static <T> boolean anyMatch(Collection<T> source, Predicate<T> predicate) {
        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        return source.stream().anyMatch(predicate);
    }

    /**
     * Checks if all elements match the predicate.
     */
    public static <T> boolean allMatch(Collection<T> source, Predicate<T> predicate) {
        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        return source.stream().allMatch(predicate);
    }

    /**
     * Checks if no elements match the predicate.
     */
    public static <T> boolean noneMatch(Collection<T> source, Predicate<T> predicate) {
        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        return source.stream().noneMatch(predicate);
    }

    /**
     * Counts elements matching the predicate.
     */
    public static <T> long count(Collection<T> source, Predicate<T> predicate) {
        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        return source.stream().filter(predicate).count();
    }

    /**
     * Returns distinct elements from collection.
     */
    public static <T> List<T> distinct(Collection<T> source) {
        Objects.requireNonNull(source, "Source collection cannot be null");

        return source.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Returns first N elements.
     */
    public static <T> List<T> takeFirst(Collection<T> source, int n) {
        Objects.requireNonNull(source, "Source collection cannot be null");
        if (n < 0) throw new IllegalArgumentException("N must be non-negative");

        return source.stream()
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Flattens a collection of collections.
     */
    public static <T> List<T> flatten(Collection<? extends Collection<T>> source) {
        Objects.requireNonNull(source, "Source collection cannot be null");

        return source.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Finds first element matching predicate, returns Optional.
     */
    public static <T> Optional<T> findFirst(Collection<T> source, Predicate<T> predicate) {
        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        return source.stream()
                .filter(predicate)
                .findFirst();
    }

    /**
     * Finds any element matching predicate, returns Optional.
     */
    public static <T> Optional<T> findAny(Collection<T> source, Predicate<T> predicate) {
        Objects.requireNonNull(source, "Source collection cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        return source.stream()
                .filter(predicate)
                .findAny();
    }

    /**
     * Safely gets element at index, returns Optional.
     */
    public static <T> Optional<T> getAt(List<T> source, int index) {
        Objects.requireNonNull(source, "Source list cannot be null");

        if (index < 0 || index >= source.size()) {
            return Optional.empty();
        }
        return Optional.ofNullable(source.get(index));
    }
}