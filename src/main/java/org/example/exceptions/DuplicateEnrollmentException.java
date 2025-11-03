package org.example.exceptions;

/**
 * Thrown when attempting to enroll a student in a course they are already enrolled in.
 * <p>
 * This runtime exception prevents duplicate course enrollments for the same student.
 */
public class DuplicateEnrollmentException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the duplicate enrollment
     */
    public DuplicateEnrollmentException(String message) {
        super(message);
    }
}