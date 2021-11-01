package com.github.sankowskiwojciech.courseslessons.service.grouplesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.group.GroupNotFoundException;
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

    public GroupLessonValidatorService(TutorRepository tutorRepository, SubdomainService subdomainService, LessonCollisionValidatorService lessonCollisionValidatorService, LessonFileValidatorService lessonFileValidatorService, FileAccessPermissionValidatorService fileAccessPermissionValidatorService, GroupRepository groupRepository) {
        super(tutorRepository, lessonCollisionValidatorService, lessonFileValidatorService, fileAccessPermissionValidatorService, subdomainService);
        this.groupRepository = groupRepository;
    }

    public GroupLesson validateCreateGroupLessonRequest(GroupLessonRequest request, String userId) {
        Lesson lesson = super.validateCreateLessonRequest(request, userId);
        subdomainService.validateIfUserHasAccessToSubdomain(request.getSubdomainAlias(), lesson.getTutorEntity().getEmailAddress());
        GroupEntity group = groupRepository.findById(request.getGroupId()).orElseThrow(GroupNotFoundException::new);
        return LessonAndGroupEntityToGroupLesson.transform(lesson, group);
    }
}
