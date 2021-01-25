package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonsSchedule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonScheduleAndStudentEntityToIndividualLessonSchedule {

    public static IndividualLessonsSchedule transform(LessonsSchedule lessonSchedule, StudentEntity studentEntity) {
        return new IndividualLessonsSchedule(lessonSchedule.getBeginningDate(), lessonSchedule.getEndDate(), lessonSchedule.getScheduleType(), lessonSchedule.getAllLessonsDurationInMinutes(), lessonSchedule.getLessonsDaysOfWeekWithTimes(), lessonSchedule.getLessonsTitles(), lessonSchedule.getOrganizationEntity(), lessonSchedule.getTutorEntity(), studentEntity);
    }
}
