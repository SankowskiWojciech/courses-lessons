package com.github.sankowskiwojciech.courseslessons.service;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.OrganizationRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.SubdomainRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainServiceImpl;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.GroupLessonsQueryProvider;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonsIdsAndListOfFilesWithoutContentProvider;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonFileValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonFileValidatorServiceImpl;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.ValidFileMIMETypes;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class ServiceConfig {
    @Autowired
    private SubdomainRepository subdomainRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private LessonFileAccessRepository lessonFileAccessRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public SubdomainService subdomainService() {
        return new SubdomainServiceImpl(organizationRepository, tutorRepository, subdomainRepository);
    }

    @Bean
    public LessonsIdsAndListOfFilesWithoutContentProvider lessonsIdsAndListOfFilesWithoutContentProvider() {
        return new LessonsIdsAndListOfFilesWithoutContentProvider(lessonFileAccessRepository, fileRepository);
    }

    @Bean
    public GroupLessonsQueryProvider groupLessonsQueryProvider() {
        return new GroupLessonsQueryProvider(entityManager);
    }

    @Bean
    public LessonFileValidatorService lessonFileValidatorService() {
        return new LessonFileValidatorServiceImpl(createDetector(), ValidFileMIMETypes.VALID_FILE_MIME_TYPES, fileRepository);
    }

    private Detector createDetector() {
        TikaConfig tikaConfig = TikaConfig.getDefaultConfig();
        return tikaConfig.getDetector();
    }
}
