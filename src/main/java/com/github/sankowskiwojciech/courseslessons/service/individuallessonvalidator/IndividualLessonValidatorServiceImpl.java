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
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonRequest;
import com.github.sankowskiwojciech.courseslessons.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.courseslessons.model.subdomain.SubdomainType;
import com.github.sankowskiwojciech.courseslessons.service.individuallessonvalidator.transformer.IndividualLessonRequestAndExternalEntitiesToIndividualLesson;
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

    @Override
    public IndividualLesson validateCreateIndividualLessonRequest(IndividualLessonRequest individualLessonRequest) {
        Subdomain subdomain = subdomainService.readSubdomainInformationIfSubdomainExists(individualLessonRequest.getSubdomainName());
        OrganizationEntity organizationEntity = readOrganizationEntityIfSubdomainIsOrganization(subdomain);
        TutorEntity tutorEntity = readTutor(individualLessonRequest.getTutorId());
        subdomainService.validateIfUserIsAllowedToAccessSubdomain(subdomain.getEmailAddress(), tutorEntity.getEmailAddress());
        StudentEntity studentEntity = readStudent(individualLessonRequest.getStudentId());
        subdomainService.validateIfUserIsAllowedToAccessSubdomain(subdomain.getEmailAddress(), studentEntity.getEmailAddress());
        return IndividualLessonRequestAndExternalEntitiesToIndividualLesson.transform(individualLessonRequest, organizationEntity, tutorEntity, studentEntity);
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
