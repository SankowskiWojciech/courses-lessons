package com.github.sankowskiwojciech.courseslessons.service.tokenvalidation;

import com.github.sankowskiwojciech.courseslessons.model.db.token.TokenEntity;

public interface TokenValidationService {

    TokenEntity validateToken(String tokenValue);

    TokenEntity validateTokenAndUser(String tokenValue, String userEmailAddress);
}
