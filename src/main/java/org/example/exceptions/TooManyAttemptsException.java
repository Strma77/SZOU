package org.example.exceptions;

/**
 * Thrown when a user exceeds the maximum number of allowed input attempts.
 * <p>
 * This exception is typically thrown after 3 consecutive invalid input attempts
 * to prevent infinite retry loops in interactive console applications.
 */
public class TooManyAttemptsException extends Exception{

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public TooManyAttemptsException(String message){
        super(message);
    }
}