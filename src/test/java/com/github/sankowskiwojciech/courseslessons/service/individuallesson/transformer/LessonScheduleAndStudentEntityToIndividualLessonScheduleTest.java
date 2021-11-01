package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.ScheduleType;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonsScheduleStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonScheduleAndStudentEntityToIndividualLessonScheduleTest {
    @Test
    public void shouldTransformCorrectly() {
        //given
        LessonsSchedule scheduleStub = IndividualLessonsScheduleStub.createWithScheduleType(ScheduleType.ONE_YEAR_LENGTH_LESSONS);
        StudentEntity studentStub = StudentEntityStub.create();

        //when
        IndividualLessonsSchedule schedule = LessonScheduleAndStudentEntityToIndividualLessonSchedule.transform(scheduleStub, studentStub);

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
        assertEquals(studentStub, schedule.getStudentEntity());
    }
}