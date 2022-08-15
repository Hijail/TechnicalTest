package com.example.technicaltest.exception;

public class InvalidBirthdateException extends RuntimeException {

    /**
     * Exception for user services method when birthdate is invalid
     *
     * @param errorMessage
     */
    public InvalidBirthdateException(String errorMessage) {
        super(errorMessage);
    }
}
