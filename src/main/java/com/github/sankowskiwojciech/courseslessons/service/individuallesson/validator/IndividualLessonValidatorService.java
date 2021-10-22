package com.github.sankowskiwojciech.courseslessons.service.individuallesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.OrganizationRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
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
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.FileAccessPermissionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonCollisionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonFileValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonValidatorService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IndividualLessonValidatorService extends LessonValidatorService {
    private final StudentRepository studentRepository;

    public IndividualLessonValidatorService(TutorRepository tutorRepository, SubdomainService subdomainService, OrganizationRepository organizationRepository, LessonCollisionValidatorService lessonCollisionValidatorService, LessonFileValidatorService lessonFileValidatorService, FileAccessPermissionValidatorService fileAccessPermissionValidatorService, StudentRepository studentRepository) {
        super(tutorRepository, lessonCollisionValidatorService, lessonFileValidatorService, fileAccessPermissionValidatorService, subdomainService, organizationRepository);
        this.studentRepository = studentRepository;
    }

    public IndividualLesson validateCreateIndividualLessonRequest(IndividualLessonRequest request) {
        Lesson lesson = super.validateCreateLessonRequest(request);
        StudentEntity student = readStudentAndValidateStudentAndTutorAccessToSubdomain(request.getSubdomainAlias(), request.getTutorId(), request.getStudentId());
        return LessonAndStudentEntityToIndividualLesson.transform(lesson, student);
    }

    public IndividualLessonsSchedule validateIndividualLessonsScheduleRequest(IndividualLessonsScheduleRequest request) {
        LessonsSchedule schedule = super.validateLessonsScheduleRequest(request);
        StudentEntity student = readStudentAndValidateStudentAndTutorAccessToSubdomain(request.getSubdomainAlias(), request.getTutorId(), request.getStudentId());
        return IndividualLessonsScheduleRequestAndExternalEntitiesToIndividualLessonsSchedule.transform(request, schedule.getOrganizationEntity(), schedule.getTutorEntity(), student);
    }

    private StudentEntity readStudentAndValidateStudentAndTutorAccessToSubdomain(String subdomainAlias, String tutorId, String studentId) {
        Optional<StudentEntity> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new StudentNotFoundException();
        }
        subdomainService.validateIfUserHasAccessToSubdomain(subdomainAlias, tutorId, studentId);
        return student.get();
    }
}
