package org.example.exceptions;

/**
 * Thrown when a negative value is provided where only positive values are allowed.
 * <p>
 * This runtime exception is typically thrown during input validation when
 * numeric values must be non-negative.
 */
public class NegativeValueException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the validation failure
     */
    public NegativeValueException(String message) {
        super(message);
    }
}