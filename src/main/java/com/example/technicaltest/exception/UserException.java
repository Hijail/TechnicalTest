package com.example.technicaltest.exception;

public class UserException  extends RuntimeException {
    /**
     * Exception for user services method
     *
     * @param errorMessage - error message
     */
    public UserException(String errorMessage) {
        super(errorMessage);
    }
}