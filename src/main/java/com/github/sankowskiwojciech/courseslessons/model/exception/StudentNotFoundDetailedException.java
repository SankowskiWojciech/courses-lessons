package com.github.sankowskiwojciech.courseslessons.model.exception;

import org.springframework.http.HttpStatus;

public class StudentNotFoundDetailedException extends DetailedException {
    private static final int statusCode = HttpStatus.NOT_FOUND.value();
    private static final String errorCode = "STUDENT_NOT_FOUND";
    private static final String errorMessage = "Student with given id not found.";

    public StudentNotFoundDetailedException() {
        super(statusCode, errorCode, errorMessage);
    }
}
