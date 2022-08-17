package com.example.technicaltest.exception;

public class InvalidPhoneException extends RuntimeException {

    /**
     * Exception for user services method when phone is invalid
     *
     * @param errorMessage - error message
     */
    public InvalidPhoneException(String errorMessage) {
        super(errorMessage);
    }
}
