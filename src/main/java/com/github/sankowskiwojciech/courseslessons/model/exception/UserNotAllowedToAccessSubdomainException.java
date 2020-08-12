package com.github.sankowskiwojciech.courseslessons.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User not allowed to access subdomain.")
public class UserNotAllowedToAccessSubdomainException extends RuntimeException {
}
