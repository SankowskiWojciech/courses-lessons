package com.github.sankowskiwojciech.courseslessons.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private final int status;
    private final String errorCode;
    private final String errorMessage;
}
