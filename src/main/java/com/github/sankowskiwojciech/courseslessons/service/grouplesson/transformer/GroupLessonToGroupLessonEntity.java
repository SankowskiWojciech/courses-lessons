package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupLessonToGroupLessonEntity implements Function<GroupLesson, GroupLessonEntity> {
    private static final GroupLessonToGroupLessonEntity INSTANCE = new GroupLessonToGroupLessonEntity();

    @Override
    public GroupLessonEntity apply(GroupLesson lesson) {
        GroupLessonEntity entity = new GroupLessonEntity();
        entity.setTitle(lesson.getTitle());
        entity.setStartDate(lesson.getStartDate());
        entity.setEndDate(lesson.getEndDate());
        entity.setDescription(lesson.getDescription());
        entity.setSubdomainEntity(lesson.getSubdomainEntity());
        entity.setTutorEntity(lesson.getTutorEntity());
        entity.setGroupEntity(lesson.getGroupEntity());
        entity.setCreationDateTime(LocalDateTime.now());
        return entity;
    }

    public static GroupLessonToGroupLessonEntity getInstance() {
        return INSTANCE;
    }
}
