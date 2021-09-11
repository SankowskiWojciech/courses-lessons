package com.github.sankowskiwojciech.courseslessons.service.grouplesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.*;
import com.github.sankowskiwojciech.coursescorelib.model.db.group.StudentsGroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.group.GroupNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateLessonException;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.LessonAndStudentsGroupEntityToGroupLesson;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.*;
import org.springframework.stereotype.Service;

@Service
public class GroupLessonValidatorService extends LessonValidatorService {

    private final SubdomainService subdomainService;
    private final StudentsGroupRepository studentsGroupRepository;

    public GroupLessonValidatorService(TutorRepository tutorRepository, SubdomainService subdomainService, OrganizationRepository organizationRepository, LessonCollisionValidatorService lessonCollisionValidatorService, LessonFileValidatorService lessonFileValidatorService, FileAccessPermissionValidatorService fileAccessPermissionValidatorService, StudentsGroupRepository studentsGroupRepository) {
        super(tutorRepository, subdomainService, organizationRepository, lessonCollisionValidatorService, lessonFileValidatorService, fileAccessPermissionValidatorService);
        this.subdomainService = subdomainService;
        this.studentsGroupRepository = studentsGroupRepository;
    }

    public GroupLesson validateCreateGroupLessonRequest(GroupLessonRequest groupLessonRequest) {
        Lesson lesson = super.validateCreateLessonRequest(groupLessonRequest);
        subdomainService.validateIfUserIsAllowedToLoginToSubdomain(groupLessonRequest.getSubdomainAlias(), groupLessonRequest.getTutorId());
        StudentsGroupEntity groupEntity = studentsGroupRepository.findById(groupLessonRequest.getGroupId()).orElseThrow(GroupNotFoundException::new);
        if (!groupEntity.getTutorEntity().getEmailAddress().equals(groupLessonRequest.getTutorId())) {
            throw new UserNotAllowedToCreateLessonException();
        }
        return LessonAndStudentsGroupEntityToGroupLesson.transform(lesson, groupEntity);
    }
}
