package com.github.sankowskiwojciech.courseslessons.service.grouplesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.subdomain.SubdomainEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.group.GroupNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateLessonException;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.LessonAndGroupEntityToGroupLesson;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.LessonsScheduleAndGroupEntityToGroupLessonsSchedule;
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
        GroupEntity group = validateRequestAndReturnGroupEntity(lesson.getSubdomainEntity(), request.getGroupId(), lesson.getTutorEntity());
        return LessonAndGroupEntityToGroupLesson.transform(lesson, group);
    }

    public GroupLessonsSchedule validateGroupLessonsScheduleRequest(GroupLessonsScheduleRequest request, String userId) {
        LessonsSchedule schedule = super.validateLessonsScheduleRequest(request, userId);
        GroupEntity group = validateRequestAndReturnGroupEntity(schedule.getSubdomainEntity(), request.getGroupId(), schedule.getTutorEntity());
        return LessonsScheduleAndGroupEntityToGroupLessonsSchedule.transform(schedule, group);
    }

    private GroupEntity validateRequestAndReturnGroupEntity(SubdomainEntity subdomain, String groupId, TutorEntity tutor) {
        subdomainService.validateIfUserHasAccessToSubdomain(subdomain.getSubdomainId(), tutor.getEmailAddress());
        GroupEntity group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        if (!subdomain.getSubdomainId().equalsIgnoreCase(group.getSubdomainEntity().getSubdomainId())) {
            throw new UserNotAllowedToCreateLessonException();
        }
        return group;
    }
}
