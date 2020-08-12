package com.github.sankowskiwojciech.courseslessons.model.tokenvalidation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class TokenValidationResponse {
    private TokenValidationResult validationResult;
}
