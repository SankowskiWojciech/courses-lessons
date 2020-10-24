package com.github.sankowskiwojciech.courseslessons.service;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileRepository;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonFileValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonFileValidatorServiceImpl;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.ValidFileMIMETypes;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Autowired
    private LessonFileRepository lessonFileRepository;

    @Bean
    public LessonFileValidatorService lessonFileValidatorService() {
        return new LessonFileValidatorServiceImpl(createDetector(), ValidFileMIMETypes.VALID_FILE_MIME_TYPES, lessonFileRepository);
    }

    private Detector createDetector() {
        TikaConfig tikaConfig = TikaConfig.getDefaultConfig();
        return tikaConfig.getDetector();
    }
}
