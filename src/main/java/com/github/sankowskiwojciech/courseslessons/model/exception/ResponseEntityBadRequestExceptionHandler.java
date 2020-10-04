package com.github.sankowskiwojciech.courseslessons.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ResponseEntityBadRequestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DetailedException.class)
    public ResponseEntity<Object> handleBadRequestException(DetailedException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getStatusCode(), exception.getErrorCode(), exception.getErrorMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(exception.getStatusCode()));
    }
}
