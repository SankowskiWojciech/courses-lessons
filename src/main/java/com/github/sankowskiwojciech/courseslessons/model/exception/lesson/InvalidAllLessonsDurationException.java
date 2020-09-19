package com.github.sankowskiwojciech.courseslessons.model.exception.lesson;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Lessons duration in minutes must be greater than 0.")
public class InvalidAllLessonsDurationException extends RuntimeException {
}
