package com.example.technicaltest.exception;

public class InvalidGenderException extends RuntimeException {

    /**
     * Exception for user services method when gender is invalid
     *
     * @param errorMessage
     */
    public InvalidGenderException(String errorMessage) {
        super(errorMessage);
    }
}
