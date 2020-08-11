package com.github.sankowskiwojciech.courseslessons.model.tokenvalidation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenValidationResponse {
    private TokenValidationResult validationResult;
}
