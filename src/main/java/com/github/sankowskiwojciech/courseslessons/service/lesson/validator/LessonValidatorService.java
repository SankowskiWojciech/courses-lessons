package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.subdomain.SubdomainEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateLessonException;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonsScheduleRequest;
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

    public Lesson validateCreateLessonRequest(LessonRequest request, String userId) {
        SubdomainEntity subdomain = subdomainService.readSubdomain(request.getSubdomainAlias());
        TutorEntity tutor = readTutor(userId);
        lessonCollisionValidatorService.validateIfNewLessonDoesNotCollideWithExistingOnes(request.getStartDate(), request.getEndDate(), tutor.getEmailAddress());
        validateFilesIds(request.getFilesIds(), tutor.getEmailAddress());
        return LessonRequestAndExternalEntitiesToLessonImpl.transform(request, subdomain, tutor);
    }

    public LessonsSchedule validateLessonsScheduleRequest(LessonsScheduleRequest request, String userId) {
        SubdomainEntity subdomain = subdomainService.readSubdomain(request.getSubdomainAlias());
        TutorEntity tutor = readTutor(userId);
        return LessonsScheduleRequestAndExternalEntitiesToLessonsSchedule.transform(request, subdomain, tutor);
    }

    private TutorEntity readTutor(String userId) {
        Optional<TutorEntity> tutor = tutorRepository.findById(userId);
        return tutor.orElseThrow(UserNotAllowedToCreateLessonException::new);
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
