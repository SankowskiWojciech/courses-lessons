package com.github.sankowskiwojciech.courseslessons.service.tokenvalidation;

import com.github.sankowskiwojciech.coursescorelib.model.db.token.TokenEntity;

public interface TokenValidationService {
    TokenEntity validateToken(String tokenValue);
}
