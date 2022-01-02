package com.github.sankowskiwojciech.courseslessons.service;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.OrganizationRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.SubdomainRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainServiceImpl;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.GroupLessonService;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.GroupLessonServiceImpl;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.GroupLessonsSchedulerService;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.GroupLessonsSchedulerServiceImpl;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.GroupLessonsQueryProvider;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.IndividualLessonService;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.IndividualLessonServiceImpl;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.IndividualLessonsSchedulerService;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.IndividualLessonsSchedulerServiceImpl;
import com.github.sankowskiwojciech.courseslessons.service.lesson.date.LessonsDatesGeneratorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.date.LessonsDatesGeneratorServiceImpl;
import com.github.sankowskiwojciech.courseslessons.service.lesson.file.LessonFileService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.file.LessonFileServiceImpl;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonsIdsAndListOfFilesWithoutContentProvider;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonCollisionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonCollisionValidatorServiceImpl;
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
    private IndividualLessonRepository individualLessonRepository;

    @Autowired
    private GroupLessonRepository groupLessonRepository;

    @Autowired
    private LessonFileAccessRepository lessonFileAccessRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public GroupLessonsQueryProvider groupLessonsQueryProvider() {
        return new GroupLessonsQueryProvider(entityManager);
    }

    @Bean
    public LessonsIdsAndListOfFilesWithoutContentProvider lessonsIdsAndListOfFilesWithoutContentProvider() {
        return new LessonsIdsAndListOfFilesWithoutContentProvider(lessonFileAccessRepository, fileRepository);
    }

    @Bean
    public LessonFileValidatorService lessonFileValidatorService() {
        return new LessonFileValidatorServiceImpl(createDetector(), ValidFileMIMETypes.VALID_FILE_MIME_TYPES, fileRepository);
    }

    @Bean
    public IndividualLessonService individualLessonService() {
        return new IndividualLessonServiceImpl(individualLessonRepository, lessonFileService(), fileRepository, lessonsIdsAndListOfFilesWithoutContentProvider());
    }

    @Bean
    public GroupLessonService groupLessonService() {
        return new GroupLessonServiceImpl(groupLessonRepository, fileRepository, lessonFileService(), lessonsIdsAndListOfFilesWithoutContentProvider(), groupLessonsQueryProvider());
    }

    @Bean
    public LessonsDatesGeneratorService lessonsDatesGeneratorService() {
        return new LessonsDatesGeneratorServiceImpl();
    }

    @Bean
    public LessonCollisionValidatorService lessonCollisionValidatorService() {
        return new LessonCollisionValidatorServiceImpl(individualLessonRepository, groupLessonRepository);
    }

    @Bean
    public IndividualLessonsSchedulerService individualLessonsSchedulerService() {
        return new IndividualLessonsSchedulerServiceImpl(individualLessonRepository, lessonsDatesGeneratorService(), lessonCollisionValidatorService());
    }

    @Bean
    public GroupLessonsSchedulerService groupLessonsSchedulerService() {
        return new GroupLessonsSchedulerServiceImpl(groupLessonRepository, lessonsDatesGeneratorService(), lessonCollisionValidatorService());
    }

    @Bean
    public LessonFileService lessonFileService() {
        return new LessonFileServiceImpl(fileRepository, lessonFileAccessRepository);
    }

    @Bean
    public SubdomainService subdomainService() {
        return new SubdomainServiceImpl(organizationRepository, tutorRepository, subdomainRepository);
    }

    private Detector createDetector() {
        TikaConfig tikaConfig = TikaConfig.getDefaultConfig();
        return tikaConfig.getDetector();
    }
}
