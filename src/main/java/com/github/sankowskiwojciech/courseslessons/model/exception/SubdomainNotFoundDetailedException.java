package com.github.sankowskiwojciech.courseslessons.model.exception;

import org.springframework.http.HttpStatus;

public class SubdomainNotFoundDetailedException extends DetailedException {
    private static final int statusCode = HttpStatus.NOT_FOUND.value();
    private static final String errorCode = "SUBDOMAIN_NOT_FOUND";
    private static final String errorMessage = "Subdomain with given name not found.";

    public SubdomainNotFoundDetailedException() {
        super(statusCode, errorCode, errorMessage);
    }
}
