package com.example.technicaltest.exception;

public class InvalidCountryException extends RuntimeException {
    public InvalidCountryException(String errorMessage) {
        super(errorMessage);
    }
}
