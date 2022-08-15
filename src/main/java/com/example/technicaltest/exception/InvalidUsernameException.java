package com.example.technicaltest.exception;

public class InvalidUsernameException extends RuntimeException {
    public InvalidUsernameException(String errorMessage) {
        super(errorMessage);
    }
}
