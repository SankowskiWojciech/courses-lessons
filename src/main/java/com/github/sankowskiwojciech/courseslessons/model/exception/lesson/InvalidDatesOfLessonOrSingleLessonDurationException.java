package com.github.sankowskiwojciech.courseslessons.model.exception.lesson;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Duration of single lesson is not equal to duration between start time and end time of lesson.")
public class InvalidDatesOfLessonOrSingleLessonDurationException extends RuntimeException {
}
