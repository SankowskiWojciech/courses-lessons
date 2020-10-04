package com.github.sankowskiwojciech.courseslessons.model.exception.lesson;

import com.github.sankowskiwojciech.courseslessons.model.exception.DetailedException;
import org.springframework.http.HttpStatus;

public class InvalidLessonTimesDetailedException extends DetailedException {
    private static final int statusCode = HttpStatus.BAD_REQUEST.value();
    private static final String errorCode = "INVALID_LESSON_TIMES";
    private static final String errorMessage = "Start time or end time of lesson is invalid.";

    public InvalidLessonTimesDetailedException() {
        super(statusCode, errorCode, errorMessage);
    }
}
