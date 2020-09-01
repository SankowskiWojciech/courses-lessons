package com.github.sankowskiwojciech.courseslessons.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "New lesson collides with existing ones.")
public class NewLessonCollidesWithExistingOnes extends RuntimeException {
}
