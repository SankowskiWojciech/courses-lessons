package com.github.sankowskiwojciech.courseslessons.service.individuallesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.*;
import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.StudentNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonsScheduleRequestAndExternalEntitiesToIndividualLessonsSchedule;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.LessonAndStudentEntityToIndividualLesson;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IndividualLessonValidatorService extends LessonValidatorService {

    private final StudentRepository studentRepository;
    private final SubdomainService subdomainService;

    public IndividualLessonValidatorService(TutorRepository tutorRepository, SubdomainService subdomainService, OrganizationRepository organizationRepository, LessonCollisionValidatorService lessonCollisionValidatorService, LessonFileValidatorService lessonFileValidatorService, FileAccessPermissionValidatorService fileAccessPermissionValidatorService, StudentRepository studentRepository) {
        super(tutorRepository, subdomainService, organizationRepository, lessonCollisionValidatorService, lessonFileValidatorService, fileAccessPermissionValidatorService);
        this.studentRepository = studentRepository;
        this.subdomainService = subdomainService;
    }

    public IndividualLesson validateCreateIndividualLessonRequest(IndividualLessonRequest individualLessonRequest) {
        Lesson lesson = super.validateCreateLessonRequest(individualLessonRequest);
        StudentEntity studentEntity = readStudentAndValidateStudentAndTutorAccessToSubdomain(individualLessonRequest.getSubdomainAlias(), individualLessonRequest.getTutorId(), individualLessonRequest.getStudentId());
        return LessonAndStudentEntityToIndividualLesson.transform(lesson, studentEntity);
    }

    public IndividualLessonsSchedule validateIndividualLessonsScheduleRequest(IndividualLessonsScheduleRequest individualLessonsScheduleRequest) {
        LessonsSchedule lessonsSchedule = super.validateLessonsScheduleRequest(individualLessonsScheduleRequest);
        StudentEntity studentEntity = readStudentAndValidateStudentAndTutorAccessToSubdomain(individualLessonsScheduleRequest.getSubdomainAlias(), individualLessonsScheduleRequest.getTutorId(), individualLessonsScheduleRequest.getStudentId());
        return IndividualLessonsScheduleRequestAndExternalEntitiesToIndividualLessonsSchedule.transform(individualLessonsScheduleRequest, lessonsSchedule.getOrganizationEntity(), lessonsSchedule.getTutorEntity(), studentEntity);
    }

    private StudentEntity readStudentAndValidateStudentAndTutorAccessToSubdomain(String subdomainAlias, String tutorId, String studentId) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(studentId);
        if (!studentEntity.isPresent()) {
            throw new StudentNotFoundException();
        }
        subdomainService.validateIfUserIsAllowedToLoginToSubdomain(subdomainAlias, tutorId, studentId);
        return studentEntity.get();
    }
}
