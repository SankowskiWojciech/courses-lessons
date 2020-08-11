package com.github.sankowskiwojciech.courseslessons.backend;

import com.github.sankowskiwojciech.courseslessons.backend.interceptor.AuthorizationHeaderHandlerInterceptor;
import com.github.sankowskiwojciech.courseslessons.backend.tokenvalidation.TokenValidationBackend;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class InterceptorAwareWebMvcConfigurer implements WebMvcConfigurer {

    private final TokenValidationBackend tokenValidationBackend;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizationHeaderHandlerInterceptor(tokenValidationBackend));
    }
}
