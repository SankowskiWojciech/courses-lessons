package com.github.sankowskiwojciech.courseslessons.model.exception.lesson;

import com.github.sankowskiwojciech.courseslessons.model.exception.DetailedException;
import org.springframework.http.HttpStatus;

public class InvalidBeginningOrEndLessonsDateDetailedException extends DetailedException {
    private static final int statusCode = HttpStatus.BAD_REQUEST.value();
    private static final String errorCode = "INVALID_SCHEDULED_LESSONS_DATES";
    private static final String errorMessage = "Beginning date or end date of scheduled lessons is invalid.";

    public InvalidBeginningOrEndLessonsDateDetailedException() {
        super(statusCode, errorCode, errorMessage);
    }
}
