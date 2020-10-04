package com.github.sankowskiwojciech.courseslessons.model.exception;

import org.springframework.http.HttpStatus;

public class UserNotAllowedToAccessSubdomainDetailedException extends DetailedException {
    private static final int statusCode = HttpStatus.FORBIDDEN.value();
    private static final String errorCode = "USER_NOT_ALLOWED_TO_ACCESS_SUBDOMAIN";
    private static final String errorMessage = "User not allowed to access subdomain.";

    public UserNotAllowedToAccessSubdomainDetailedException() {
        super(statusCode, errorCode, errorMessage);
    }
}
