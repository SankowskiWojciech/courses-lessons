package com.github.sankowskiwojciech.courseslessons.service.tokenvalidation;

public interface TokenValidationService {

    void validateToken(String tokenValue);

    void validateTokenAndUser(String tokenValue, String userEmailAddress);
}
