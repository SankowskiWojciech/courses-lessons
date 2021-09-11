package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.group.StudentsGroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonAndStudentsGroupEntityToGroupLesson {

    public static GroupLesson transform(Lesson lesson, StudentsGroupEntity groupEntity) {
        return new GroupLesson(lesson.getTitle(), lesson.getStartDateOfLesson(), lesson.getEndDateOfLesson(), lesson.getDescription(), lesson.getOrganizationEntity(), lesson.getTutorEntity(), lesson.getFilesIds(), groupEntity);
    }
}
