package com.github.sankowskiwojciech.courseslessons.service.grouplesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.OrganizationRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.group.GroupNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateLessonException;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.LessonAndGroupEntityToGroupLesson;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.FileAccessPermissionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonCollisionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonFileValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonValidatorService;
import org.springframework.stereotype.Service;

@Service
public class GroupLessonValidatorService extends LessonValidatorService {
    private final GroupRepository groupRepository;

    public GroupLessonValidatorService(TutorRepository tutorRepository, SubdomainService subdomainService, OrganizationRepository organizationRepository, LessonCollisionValidatorService lessonCollisionValidatorService, LessonFileValidatorService lessonFileValidatorService, FileAccessPermissionValidatorService fileAccessPermissionValidatorService, GroupRepository groupRepository) {
        super(tutorRepository, lessonCollisionValidatorService, lessonFileValidatorService, fileAccessPermissionValidatorService, subdomainService, organizationRepository);
        this.groupRepository = groupRepository;
    }

    public GroupLesson validateCreateGroupLessonRequest(GroupLessonRequest request) {
        Lesson lesson = super.validateCreateLessonRequest(request);
        subdomainService.validateIfUserHasAccessToSubdomain(request.getSubdomainAlias(), request.getTutorId());
        GroupEntity group = groupRepository.findById(request.getGroupId()).orElseThrow(GroupNotFoundException::new);
        if (userIsNotOwnerOfGroup(group, request)) {
            throw new UserNotAllowedToCreateLessonException();
        }
        return LessonAndGroupEntityToGroupLesson.transform(lesson, group);
    }

    private boolean userIsNotOwnerOfGroup(GroupEntity group, GroupLessonRequest request) {
        return !group.getTutorEntity().getEmailAddress().equals(request.getTutorId());
    }
}
