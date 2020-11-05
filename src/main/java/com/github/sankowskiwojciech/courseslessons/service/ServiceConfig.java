package com.github.sankowskiwojciech.courseslessons.service;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileRepository;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.IndividualLessonService;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.IndividualLessonServiceImpl;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider;
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

    @Autowired
    private IndividualLessonRepository individualLessonRepository;

    @Autowired
    private IndividualLessonFileRepository individualLessonFileRepository;

    @Bean
    public IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider() {
        return new IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider(individualLessonFileRepository, lessonFileRepository);
    }

    @Bean
    public LessonFileValidatorService lessonFileValidatorService() {
        return new LessonFileValidatorServiceImpl(createDetector(), ValidFileMIMETypes.VALID_FILE_MIME_TYPES, lessonFileRepository);
    }

    @Bean
    public IndividualLessonService individualLessonService() {
        return new IndividualLessonServiceImpl(individualLessonRepository, individualLessonFileRepository, lessonFileRepository, individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider());
    }

    private Detector createDetector() {
        TikaConfig tikaConfig = TikaConfig.getDefaultConfig();
        return tikaConfig.getDetector();
    }
}
