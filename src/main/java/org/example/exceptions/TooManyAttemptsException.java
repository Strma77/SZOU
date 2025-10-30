package org.example.exceptions;

public class TooManyAttemptsException extends Exception{
    public TooManyAttemptsException(String message){
        super(message);
    }
}