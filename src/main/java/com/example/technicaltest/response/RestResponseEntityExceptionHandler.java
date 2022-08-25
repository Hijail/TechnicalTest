package com.example.technicaltest.response;

import com.example.technicaltest.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    /**
     * catch exception and create response for post error
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value
            = { InvalidBirthdateException.class, InvalidCountryException.class, InvalidGenderException.class,
            InvalidPhoneException.class, InvalidUsernameException.class })
    protected ResponseEntity<Object> handleCreateUserError(
            RuntimeException ex, WebRequest request) {
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    /**
     * catch exception and create response for get error
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = { UserException.class })
    protected ResponseEntity<Object> handleGetUserError(
            RuntimeException ex, WebRequest request) {
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_FOUND, null);
    }
}
