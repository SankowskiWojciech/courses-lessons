package com.github.sankowskiwojciech.courseslessons.model.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenDetailedException extends DetailedException {
    private static final int statusCode = HttpStatus.UNAUTHORIZED.value();
    private static final String errorCode = "INVALID_TOKEN";
    private static final String errorMessage = "Token is invalid.";

    public InvalidTokenDetailedException() {
        super(statusCode, errorCode, errorMessage);
    }
}
