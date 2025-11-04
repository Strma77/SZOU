package org.example.exceptions;

/**
 * Thrown when a user attempts to exceed their maximum allowed course limit.
 * <p>
 * This runtime exception enforces course enrollment or teaching limits for
 * students and professors based on their configured maximum capacity.
 */
public class LimitExceededException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the limit violation
     */
    public LimitExceededException(String message) {
        super(message);
    }
}