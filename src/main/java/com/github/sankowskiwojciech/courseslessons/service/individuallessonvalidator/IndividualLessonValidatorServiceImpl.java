package com.github.sankowskiwojciech.courseslessons.service.individuallessonvalidator;

import com.github.sankowskiwojciech.courseslessons.backend.repository.OrganizationRepository;
import com.github.sankowskiwojciech.courseslessons.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.courseslessons.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.courseslessons.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.courseslessons.model.exception.StudentNotFoundException;
import com.github.sankowskiwojciech.courseslessons.model.exception.UserNotAllowedToCreateLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.request.IndividualLessonRequest;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.courseslessons.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.courseslessons.model.subdomain.SubdomainType;
import com.github.sankowskiwojciech.courseslessons.service.individuallessonvalidator.transformer.IndividualLessonRequestAndExternalEntitiesToIndividualLesson;
import com.github.sankowskiwojciech.courseslessons.service.individuallessonvalidator.transformer.IndividualLessonsScheduleRequestAndExternalEntitiesToIndividualLessonsSchedule;
import com.github.sankowskiwojciech.courseslessons.service.lessonvalidator.LessonCollisionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.subdomain.SubdomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class IndividualLessonValidatorServiceImpl implements IndividualLessonValidatorService {

    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final SubdomainService subdomainService;
    private final OrganizationRepository organizationRepository;
    private final LessonCollisionValidatorService lessonCollisionValidatorService;

    @Override
    public IndividualLesson validateCreateIndividualLessonRequest(IndividualLessonRequest individualLessonRequest) {
        Subdomain subdomain = subdomainService.readSubdomainInformationIfSubdomainExists(individualLessonRequest.getSubdomainName());
        OrganizationEntity organizationEntity = readOrganizationEntityIfSubdomainIsOrganization(subdomain);
        TutorEntity tutorEntity = readTutor(individualLessonRequest.getTutorId());
        subdomainService.validateIfUserIsAllowedToAccessSubdomain(subdomain.getEmailAddress(), tutorEntity.getEmailAddress());
        StudentEntity studentEntity = readStudent(individualLessonRequest.getStudentId());
        subdomainService.validateIfUserIsAllowedToAccessSubdomain(subdomain.getEmailAddress(), studentEntity.getEmailAddress());
        String organizationEmailAddress = organizationEntity != null ? organizationEntity.getEmailAddress() : null;
        lessonCollisionValidatorService.validateIfNewLessonDoesNotCollideWithExistingOnes(individualLessonRequest.getStartDateOfLesson(), individualLessonRequest.getEndDateOfLesson(), tutorEntity.getEmailAddress(), organizationEmailAddress);
        return IndividualLessonRequestAndExternalEntitiesToIndividualLesson.transform(individualLessonRequest, organizationEntity, tutorEntity, studentEntity);
    }

    @Override
    public IndividualLessonsSchedule validateIndividualLessonsScheduleRequest(IndividualLessonsScheduleRequest individualLessonsScheduleRequest) {
        Subdomain subdomain = subdomainService.readSubdomainInformationIfSubdomainExists(individualLessonsScheduleRequest.getSubdomainName());
        OrganizationEntity organizationEntity = readOrganizationEntityIfSubdomainIsOrganization(subdomain);
        TutorEntity tutorEntity = readTutor(individualLessonsScheduleRequest.getTutorId());
        subdomainService.validateIfUserIsAllowedToAccessSubdomain(subdomain.getEmailAddress(), tutorEntity.getEmailAddress());
        StudentEntity studentEntity = readStudent(individualLessonsScheduleRequest.getStudentId());
        subdomainService.validateIfUserIsAllowedToAccessSubdomain(subdomain.getEmailAddress(), studentEntity.getEmailAddress());
        return IndividualLessonsScheduleRequestAndExternalEntitiesToIndividualLessonsSchedule.transform(individualLessonsScheduleRequest, organizationEntity, tutorEntity, studentEntity);
    }

    private OrganizationEntity readOrganizationEntityIfSubdomainIsOrganization(Subdomain subdomain) {
        if (SubdomainType.ORGANIZATION.equals(subdomain.getSubdomainType())) {
            return organizationRepository.findById(subdomain.getEmailAddress()).get();
        }
        return null;
    }

    private TutorEntity readTutor(String tutorId) {
        Optional<TutorEntity> tutorEntity = tutorRepository.findById(tutorId);
        if (!tutorEntity.isPresent()) {
            throw new UserNotAllowedToCreateLesson();
        }
        return tutorEntity.get();
    }

    private StudentEntity readStudent(String studentId) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(studentId);
        if (!studentEntity.isPresent()) {
            throw new StudentNotFoundException();
        }
        return studentEntity.get();
    }
}
