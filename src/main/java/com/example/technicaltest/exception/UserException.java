package com.example.technicaltest.exception;

public class UserException  extends RuntimeException {
    public UserException(String errorMessage) {
        super(errorMessage);
    }
}