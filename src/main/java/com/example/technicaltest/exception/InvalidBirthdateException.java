package com.example.technicaltest.exception;

public class InvalidBirthdateException extends RuntimeException {
    public InvalidBirthdateException(String errorMessage) {
        super(errorMessage);
    }
}
