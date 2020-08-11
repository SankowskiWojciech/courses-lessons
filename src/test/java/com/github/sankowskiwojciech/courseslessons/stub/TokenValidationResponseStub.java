package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.courseslessons.model.tokenvalidation.TokenValidationResponse;
import com.github.sankowskiwojciech.courseslessons.model.tokenvalidation.TokenValidationResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenValidationResponseStub {

    public static TokenValidationResponse create(TokenValidationResult tokenValidationResult) {
        return new TokenValidationResponse(tokenValidationResult);
    }
}
