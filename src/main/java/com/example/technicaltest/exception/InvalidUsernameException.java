package com.example.technicaltest.exception;

public class InvalidUsernameException extends RuntimeException {

    /**
     * Exception for user services method when username is invalid
     *
     * @param errorMessage
     */
    public InvalidUsernameException(String errorMessage) {
        super(errorMessage);
    }
}
