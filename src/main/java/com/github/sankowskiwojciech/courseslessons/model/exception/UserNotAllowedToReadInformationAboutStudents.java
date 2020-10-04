package com.github.sankowskiwojciech.courseslessons.model.exception;

import org.springframework.http.HttpStatus;

public class UserNotAllowedToReadInformationAboutStudents extends DetailedException {
    private static final int statusCode = HttpStatus.FORBIDDEN.value();
    private static final String errorCode = "USER_NOT_ALLOWED_TO_ACCESS_INFORMATION_ABOUT_STUDENTS";
    private static final String errorMessage = "User is not allowed to access information about students.";

    public UserNotAllowedToReadInformationAboutStudents() {
        super(statusCode, errorCode, errorMessage);
    }
}
