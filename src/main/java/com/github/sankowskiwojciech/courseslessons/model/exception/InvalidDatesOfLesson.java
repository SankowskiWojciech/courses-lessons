package com.github.sankowskiwojciech.courseslessons.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Start date or end date of lesson is invalid.")
public class InvalidDatesOfLesson extends RuntimeException {
}
