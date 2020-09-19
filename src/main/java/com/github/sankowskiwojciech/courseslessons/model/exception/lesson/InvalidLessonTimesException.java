package com.github.sankowskiwojciech.courseslessons.model.exception.lesson;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Start time or end time of lesson is invalid.")
public class InvalidLessonTimesException extends RuntimeException {
}
