package com.github.sankowskiwojciech.courseslessons.controller;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

@Configuration
@Component
public class MultipartConfigElementConfig {

    private static final DataSize FILE_UPLOAD_MAX_SIZE = DataSize.of(50, DataUnit.MEGABYTES);

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(FILE_UPLOAD_MAX_SIZE);
        factory.setMaxRequestSize(FILE_UPLOAD_MAX_SIZE);
        return factory.createMultipartConfig();
    }
}