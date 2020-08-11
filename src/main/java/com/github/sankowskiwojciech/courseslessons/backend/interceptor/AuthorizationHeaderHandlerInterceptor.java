package com.github.sankowskiwojciech.courseslessons.backend.interceptor;

import com.github.sankowskiwojciech.courseslessons.backend.tokenvalidation.TokenValidationBackend;
import com.github.sankowskiwojciech.courseslessons.model.exception.InvalidTokenException;
import com.github.sankowskiwojciech.courseslessons.model.tokenvalidation.TokenValidationInput;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
public class AuthorizationHeaderHandlerInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private final TokenValidationBackend tokenValidationBackend;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorizationHeaderValue = getAuthorizationHeaderValue(request);
        tokenValidationBackend.validateToken(new TokenValidationInput(authorizationHeaderValue));
        return true;
    }

    private String getAuthorizationHeaderValue(HttpServletRequest httpServletRequest) {
        String authorizationHeaderValue = httpServletRequest.getHeader(AUTHORIZATION_HEADER_NAME);
        if (StringUtils.isBlank(authorizationHeaderValue)) {
            throw new InvalidTokenException();
        }
        return authorizationHeaderValue;
    }
}
