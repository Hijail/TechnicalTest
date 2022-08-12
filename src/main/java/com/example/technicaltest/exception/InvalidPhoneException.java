package com.example.technicaltest.exception;

public class InvalidPhoneException extends RuntimeException {
    public InvalidPhoneException(String errorMessage) {
        super(errorMessage);
    }
}
