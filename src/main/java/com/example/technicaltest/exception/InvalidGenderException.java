package com.example.technicaltest.exception;

public class InvalidGenderException extends RuntimeException {
    public InvalidGenderException(String errorMessage) {
        super(errorMessage);
    }
}
