package com.github.sankowskiwojciech.courseslessons.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class DetailedException extends RuntimeException {
    private final int statusCode;
    private final String errorCode;
    private final String errorMessage;
}
