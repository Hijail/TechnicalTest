package com.example.technicaltest.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    private ResponseHandler() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class");
    }

    /**
     * generateResponse
     *
     * Method which allows to create the API responses
     *
     * @param message response message
     * @param status response status
     * @param responseObj response object
     * @return
     */
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", responseObj);
        map.put("status", status.value());
        map.put("message", message);

        return new ResponseEntity<>(map,status);
    }
}