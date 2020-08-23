package com.github.sankowskiwojciech.courseslessons.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User not allowed to read information about students.")
public class UserNotAllowedToReadInformationAboutStudents extends RuntimeException {
}
