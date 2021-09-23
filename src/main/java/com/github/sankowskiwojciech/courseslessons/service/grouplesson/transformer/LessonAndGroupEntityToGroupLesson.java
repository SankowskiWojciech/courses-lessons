package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonAndGroupEntityToGroupLesson {
    public static GroupLesson transform(Lesson lesson, GroupEntity groupEntity) {
        return new GroupLesson(lesson.getTitle(), lesson.getStartDate(), lesson.getEndDate(), lesson.getDescription(), lesson.getOrganizationEntity(), lesson.getTutorEntity(), lesson.getFilesIds(), groupEntity);
    }
}
