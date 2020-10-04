package com.github.sankowskiwojciech.courseslessons.model.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestBodyDetailedException extends DetailedException {
    private static final int statusCode = HttpStatus.BAD_REQUEST.value();
    private static final String errorCode = "INVALID_REQUEST_BODY";
    private static final String errorMessage = "Invalid request body.";

    public InvalidRequestBodyDetailedException() {
        super(statusCode, errorCode, errorMessage);
    }
}
