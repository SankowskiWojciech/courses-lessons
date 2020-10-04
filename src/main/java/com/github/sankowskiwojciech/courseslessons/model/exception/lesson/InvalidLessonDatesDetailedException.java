package com.github.sankowskiwojciech.courseslessons.model.exception.lesson;

import com.github.sankowskiwojciech.courseslessons.model.exception.DetailedException;
import org.springframework.http.HttpStatus;

public class InvalidLessonDatesDetailedException extends DetailedException {
    private static final int statusCode = HttpStatus.BAD_REQUEST.value();
    private static final String errorCode = "INVALID_LESSON_DATES";
    private static final String errorMessage = "Start date or end date of lesson is invalid.";

    public InvalidLessonDatesDetailedException() {
        super(statusCode, errorCode, errorMessage);
    }
}
