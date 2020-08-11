package com.github.sankowskiwojciech.courseslessons.backend.tokenvalidation;

import com.github.sankowskiwojciech.courseslessons.model.tokenvalidation.TokenValidationInput;

public interface TokenValidationBackend {

    void validateToken(TokenValidationInput tokenValidationInput);
}
