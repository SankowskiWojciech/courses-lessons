package com.github.sankowskiwojciech.courseslessons.model.exception.lesson;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Beginning date or end date of lessons is invalid.")
public class InvalidBeginningOrEndLessonsDateException extends RuntimeException {
}
