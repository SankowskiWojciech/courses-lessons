package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonsSchedule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonScheduleAndStudentEntityToIndividualLessonSchedule {

    public static IndividualLessonsSchedule transform(LessonsSchedule schedule, StudentEntity student) {
        return new IndividualLessonsSchedule(schedule.getBeginningDate(), schedule.getEndDate(), schedule.getScheduleType(), schedule.getAllLessonsDurationInMinutes(), schedule.getDaysOfWeekWithTimes(), schedule.getTitles(), schedule.getOrganizationEntity(), schedule.getTutorEntity(), student);
    }
}
