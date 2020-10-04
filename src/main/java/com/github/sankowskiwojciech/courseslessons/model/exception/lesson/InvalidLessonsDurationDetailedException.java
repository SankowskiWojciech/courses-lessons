package com.github.sankowskiwojciech.courseslessons.model.exception.lesson;

import com.github.sankowskiwojciech.courseslessons.model.exception.DetailedException;
import org.springframework.http.HttpStatus;

public class InvalidLessonsDurationDetailedException extends DetailedException {
    private static final int statusCode = HttpStatus.BAD_REQUEST.value();
    private static final String errorCode = "INVALID_SCHEDULED_LESSONS_DURATION";
    private static final String errorMessage = "Scheduled duration in minutes is invalid.";

    public InvalidLessonsDurationDetailedException() {
        super(statusCode, errorCode, errorMessage);
    }
}
