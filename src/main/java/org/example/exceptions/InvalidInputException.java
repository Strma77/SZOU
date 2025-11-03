package org.example.exceptions;

/**
 * Thrown when user input does not meet expected format or validation criteria.
 * <p>
 * This checked exception is typically thrown during input validation when
 * the provided value is empty, non-numeric, or otherwise invalid.
 */
public class InvalidInputException extends Exception{

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the validation failure
     */
    public InvalidInputException (String message){
        super(message);
    }
}