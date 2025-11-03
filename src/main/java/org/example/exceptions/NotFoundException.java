package org.example.exceptions;

/**
 * Thrown when a requested entity or resource cannot be found.
 * <p>
 * This runtime exception is typically thrown during search operations when
 * no matching student, professor, or course is found.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining what was not found
     */
    public NotFoundException(String message) {
        super(message);
    }
}