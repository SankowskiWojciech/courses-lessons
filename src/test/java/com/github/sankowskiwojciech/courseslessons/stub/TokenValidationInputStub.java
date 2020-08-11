package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.courseslessons.model.tokenvalidation.TokenValidationInput;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TOKEN_VALUE_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenValidationInputStub {

    public static TokenValidationInput create() {
        return new TokenValidationInput(TOKEN_VALUE_STUB);
    }
}
