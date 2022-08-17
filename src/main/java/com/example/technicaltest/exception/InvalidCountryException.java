package com.example.technicaltest.exception;

public class InvalidCountryException extends RuntimeException {

    /**
     * Exception for user services method when country is invalid
     *
     * @param errorMessage - error message
     */
    public InvalidCountryException(String errorMessage) {
        super(errorMessage);
    }
}
