package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.ScheduleType;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupLessonsScheduleStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonsScheduleAndGroupEntityToGroupLessonsScheduleTest {
    @Test
    public void shouldTransformCorrectly() {
        //given
        LessonsSchedule scheduleStub = GroupLessonsScheduleStub.createWithScheduleType(ScheduleType.ONE_YEAR_LENGTH_LESSONS);
        GroupEntity groupStub = GroupEntityStub.create();

        //when
        GroupLessonsSchedule schedule = LessonsScheduleAndGroupEntityToGroupLessonsSchedule.transform(scheduleStub, groupStub);

        //then
        assertNotNull(schedule);
        assertEquals(scheduleStub.getBeginningDate(), schedule.getBeginningDate());
        assertEquals(scheduleStub.getEndDate(), schedule.getEndDate());
        assertEquals(scheduleStub.getScheduleType(), schedule.getScheduleType());
        assertEquals(scheduleStub.getAllLessonsDurationInMinutes(), schedule.getAllLessonsDurationInMinutes());
        assertEquals(scheduleStub.getDaysOfWeekWithTimes(), schedule.getDaysOfWeekWithTimes());
        assertEquals(scheduleStub.getTitles(), schedule.getTitles());
        assertEquals(scheduleStub.getSubdomainEntity(), schedule.getSubdomainEntity());
        assertEquals(scheduleStub.getTutorEntity(), schedule.getTutorEntity());
        assertEquals(groupStub, schedule.getGroupEntity());
    }
}