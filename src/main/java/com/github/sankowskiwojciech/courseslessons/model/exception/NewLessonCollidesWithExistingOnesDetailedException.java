package com.github.sankowskiwojciech.courseslessons.model.exception;

import org.springframework.http.HttpStatus;

public class NewLessonCollidesWithExistingOnesDetailedException extends DetailedException {
    private static final int statusCode = HttpStatus.BAD_REQUEST.value();
    private static final String errorCode = "LESSONS_DATES_COLLISION";
    private static final String errorMessage = "Dates of lesson collide with existing lessons.";

    public NewLessonCollidesWithExistingOnesDetailedException() {
        super(statusCode, errorCode, errorMessage);
    }
}
