package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.OrganizationRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateLessonException;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.SubdomainType;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonRequestAndExternalEntitiesToLessonImpl;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonsScheduleRequestAndExternalEntitiesToLessonsSchedule;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public abstract class LessonValidatorService {
    private final TutorRepository tutorRepository;
    private final SubdomainService subdomainService;
    private final OrganizationRepository organizationRepository;
    private final LessonCollisionValidatorService lessonCollisionValidatorService;
    private final LessonFileValidatorService lessonFileValidatorService;
    private final FileAccessPermissionValidatorService fileAccessPermissionValidatorService;

    public Lesson validateCreateLessonRequest(LessonRequest lessonRequest) {
        Subdomain subdomain = subdomainService.readSubdomainInformation(lessonRequest.getSubdomainAlias());
        OrganizationEntity organizationEntity = readOrganizationEntityIfSubdomainIsOrganization(subdomain);
        TutorEntity tutorEntity = readTutor(lessonRequest.getTutorId());
        lessonCollisionValidatorService.validateIfNewLessonDoesNotCollideWithExistingOnes(lessonRequest.getStartDateOfLesson(), lessonRequest.getEndDateOfLesson(), tutorEntity.getEmailAddress());
        validateFilesIds(lessonRequest.getFilesIds(), lessonRequest.getTutorId());
        return LessonRequestAndExternalEntitiesToLessonImpl.transform(lessonRequest, organizationEntity, tutorEntity);
    }

    public LessonsSchedule validateLessonsScheduleRequest(LessonsScheduleRequest lessonsScheduleRequest) {
        Subdomain subdomain = subdomainService.readSubdomainInformation(lessonsScheduleRequest.getSubdomainAlias());
        OrganizationEntity organizationEntity = readOrganizationEntityIfSubdomainIsOrganization(subdomain);
        TutorEntity tutorEntity = readTutor(lessonsScheduleRequest.getTutorId());
        return LessonsScheduleRequestAndExternalEntitiesToLessonsSchedule.transform(lessonsScheduleRequest, organizationEntity, tutorEntity);
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
            throw new UserNotAllowedToCreateLessonException();
        }
        return tutorEntity.get();
    }

    private void validateFilesIds(List<String> filesIds, String tutorId) {
        if (CollectionUtils.isNotEmpty(filesIds)) {
            filesIds.forEach(fileId -> {
                lessonFileValidatorService.validateIfFileExists(fileId);
                fileAccessPermissionValidatorService.validateIfUserIsAllowedToAccessFile(tutorId, fileId);
            });
        }
    }
}
