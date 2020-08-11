package com.github.sankowskiwojciech.courseslessons.backend;

import com.github.sankowskiwojciech.courseslessons.backend.tokenvalidation.TokenValidationBackend;
import com.github.sankowskiwojciech.courseslessons.backend.tokenvalidation.TokenValidationBackendImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BackendConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TokenValidationBackend tokenValidationBackend(RestTemplate restTemplate) {
        return new TokenValidationBackendImpl(restTemplate);
    }
}
