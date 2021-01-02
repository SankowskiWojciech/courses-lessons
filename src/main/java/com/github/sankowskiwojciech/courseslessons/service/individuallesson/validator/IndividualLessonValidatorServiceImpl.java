package com.github.sankowskiwojciech.courseslessons.service.individuallesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.OrganizationRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.StudentNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateLessonException;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.SubdomainType;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonRequestAndExternalEntitiesToIndividualLesson;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonsScheduleRequestAndExternalEntitiesToIndividualLessonsSchedule;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.FileAccessPermissionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonCollisionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonFileValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IndividualLessonValidatorServiceImpl implements IndividualLessonValidatorService {

    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final SubdomainService subdomainService;
    private final OrganizationRepository organizationRepository;
    private final LessonCollisionValidatorService lessonCollisionValidatorService;
    private final LessonFileValidatorService lessonFileValidatorService;
    private final FileAccessPermissionValidatorService fileAccessPermissionValidatorService;

    @Override
    public IndividualLesson validateCreateIndividualLessonRequest(IndividualLessonRequest individualLessonRequest) {
        Subdomain subdomain = subdomainService.readSubdomainInformation(individualLessonRequest.getSubdomainName());
        OrganizationEntity organizationEntity = readOrganizationEntityIfSubdomainIsOrganization(subdomain);
        TutorEntity tutorEntity = readTutor(individualLessonRequest.getTutorId());
        StudentEntity studentEntity = readStudent(individualLessonRequest.getStudentId());
        subdomainService.validateIfUserIsAllowedToLoginToSubdomain(individualLessonRequest.getSubdomainName(), tutorEntity.getEmailAddress(), studentEntity.getEmailAddress());
        String organizationEmailAddress = organizationEntity != null ? organizationEntity.getEmailAddress() : null;
        lessonCollisionValidatorService.validateIfNewLessonDoesNotCollideWithExistingOnes(individualLessonRequest.getStartDateOfLesson(), individualLessonRequest.getEndDateOfLesson(), tutorEntity.getEmailAddress(), organizationEmailAddress);
        validateFilesIds(individualLessonRequest.getFilesIds(), individualLessonRequest.getTutorId());
        return IndividualLessonRequestAndExternalEntitiesToIndividualLesson.transform(individualLessonRequest, organizationEntity, tutorEntity, studentEntity);
    }

    @Override
    public IndividualLessonsSchedule validateIndividualLessonsScheduleRequest(IndividualLessonsScheduleRequest individualLessonsScheduleRequest) {
        Subdomain subdomain = subdomainService.readSubdomainInformation(individualLessonsScheduleRequest.getSubdomainName());
        OrganizationEntity organizationEntity = readOrganizationEntityIfSubdomainIsOrganization(subdomain);
        TutorEntity tutorEntity = readTutor(individualLessonsScheduleRequest.getTutorId());
        StudentEntity studentEntity = readStudent(individualLessonsScheduleRequest.getStudentId());
        subdomainService.validateIfUserIsAllowedToLoginToSubdomain(individualLessonsScheduleRequest.getSubdomainName(), tutorEntity.getEmailAddress(), studentEntity.getEmailAddress());
        return IndividualLessonsScheduleRequestAndExternalEntitiesToIndividualLessonsSchedule.transform(individualLessonsScheduleRequest, organizationEntity, tutorEntity, studentEntity);
    }

    private OrganizationEntity readOrganizationEntityIfSubdomainIsOrganization(Subdomain subdomain) {
        if (SubdomainType.ORGANIZATION.equals(subdomain.getSubdomainType())) {
            return organizationRepository.findById(subdomain.getEmailAddress()).get();
        }
        return null;
    }

    private TutorEntity readTutor(String tutorId) {
        return tutorRepository.findById(tutorId).orElseThrow(UserNotAllowedToCreateLessonException::new);
    }

    private StudentEntity readStudent(String studentId) {
        return studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);
    }

    private void validateFilesIds(List<Long> filesIds, String tutorId) {
        if (filesIds != null && !filesIds.isEmpty()) {
            filesIds.forEach(fileId -> {
                lessonFileValidatorService.validateIfFileExists(fileId);
                fileAccessPermissionValidatorService.validateIfUserIsAllowedToAccessFile(tutorId, fileId);
            });
        }
    }
}
