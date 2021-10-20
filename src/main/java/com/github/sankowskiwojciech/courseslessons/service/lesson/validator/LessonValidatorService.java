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
    private final LessonCollisionValidatorService lessonCollisionValidatorService;
    private final LessonFileValidatorService lessonFileValidatorService;
    private final FileAccessPermissionValidatorService fileAccessPermissionValidatorService;
    protected final SubdomainService subdomainService;
    protected final OrganizationRepository organizationRepository;

    public Lesson validateCreateLessonRequest(LessonRequest request) {
        Subdomain subdomain = subdomainService.readSubdomainInformation(request.getSubdomainAlias());
        OrganizationEntity organization = readOrganizationIfSubdomainIsOrganization(subdomain);
        TutorEntity tutor = readTutor(request.getTutorId());
        lessonCollisionValidatorService.validateIfNewLessonDoesNotCollideWithExistingOnes(request.getStartDate(), request.getEndDate(), tutor.getEmailAddress());
        validateFilesIds(request.getFilesIds(), request.getTutorId());
        return LessonRequestAndExternalEntitiesToLessonImpl.transform(request, organization, tutor);
    }

    public LessonsSchedule validateLessonsScheduleRequest(LessonsScheduleRequest request) {
        Subdomain subdomain = subdomainService.readSubdomainInformation(request.getSubdomainAlias());
        OrganizationEntity organization = readOrganizationIfSubdomainIsOrganization(subdomain);
        TutorEntity tutor = readTutor(request.getTutorId());
        return LessonsScheduleRequestAndExternalEntitiesToLessonsSchedule.transform(request, organization, tutor);
    }

    private OrganizationEntity readOrganizationIfSubdomainIsOrganization(Subdomain subdomain) {
        if (SubdomainType.ORGANIZATION.equals(subdomain.getSubdomainType())) {
            return organizationRepository.findById(subdomain.getEmailAddress()).get();
        }
        return null;
    }

    private TutorEntity readTutor(String tutorId) {
        Optional<TutorEntity> tutor = tutorRepository.findById(tutorId);
        if (!tutor.isPresent()) {
            throw new UserNotAllowedToCreateLessonException();
        }
        return tutor.get();
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
