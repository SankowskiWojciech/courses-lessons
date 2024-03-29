package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonAndStudentEntityToIndividualLesson {
    public static IndividualLesson transform(Lesson lesson, StudentEntity student) {
        return new IndividualLesson(lesson.getTitle(), lesson.getStartDate(), lesson.getEndDate(), lesson.getDescription(), lesson.getSubdomainEntity(), lesson.getTutorEntity(), lesson.getFilesIds(), student);
    }
}
