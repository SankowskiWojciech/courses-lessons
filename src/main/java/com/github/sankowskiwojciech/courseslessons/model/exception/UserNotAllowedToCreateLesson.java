package com.github.sankowskiwojciech.courseslessons.model.exception;

import org.springframework.http.HttpStatus;

public class UserNotAllowedToCreateLesson extends DetailedException {
    private static final int statusCode = HttpStatus.FORBIDDEN.value();
    private static final String errorCode = "USER_NOT_ALLOWED_TO_CREATE_LESSONS";
    private static final String errorMessage = "User not allowed to create lessons.";

    public UserNotAllowedToCreateLesson() {
        super(statusCode, errorCode, errorMessage);
    }
}
