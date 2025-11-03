package org.example.exceptions;

/**
 * Thrown when a count or quantity value is invalid or out of acceptable range.
 * <p>
 * This runtime exception is typically thrown when validating collection sizes,
 * array lengths, or quantity parameters that must meet specific constraints.
 */
public class InvalidCountException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining why the count is invalid
     */
    public InvalidCountException(String message) {
        super(message);
    }
}