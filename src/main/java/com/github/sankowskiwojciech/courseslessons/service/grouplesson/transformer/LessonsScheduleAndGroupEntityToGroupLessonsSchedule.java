package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonsSchedule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonsScheduleAndGroupEntityToGroupLessonsSchedule {
    public static GroupLessonsSchedule transform(LessonsSchedule schedule, GroupEntity group) {
        return new GroupLessonsSchedule(schedule.getBeginningDate(), schedule.getEndDate(), schedule.getScheduleType(), schedule.getAllLessonsDurationInMinutes(), schedule.getDaysOfWeekWithTimes(), schedule.getTitles(), group.getSubdomainEntity(), group.getTutorEntity(), group);
    }
}
