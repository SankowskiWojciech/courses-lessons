package com.github.sankowskiwojciech.courseslessons.controller;

import com.github.sankowskiwojciech.courseslessons.controller.validator.SubdomainAndUserAccessValidator;
import com.github.sankowskiwojciech.courseslessons.service.subdomain.SubdomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {

    @Autowired
    private SubdomainService subdomainService;

    @Bean
    public SubdomainAndUserAccessValidator subdomainAndUserAccessValidator() {
        return new SubdomainAndUserAccessValidator(subdomainService);
    }
}
