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
        LessonsSchedule lessonsScheduleStub = IndividualLessonsScheduleStub.createWithScheduleType(ScheduleType.ONE_YEAR_LENGTH_LESSONS);
        StudentEntity studentEntityStub = StudentEntityStub.create();

        //when
        IndividualLessonsSchedule individualLessonsSchedule = LessonScheduleAndStudentEntityToIndividualLessonSchedule.transform(lessonsScheduleStub, studentEntityStub);

        //then
        assertNotNull(individualLessonsSchedule);
        assertEquals(lessonsScheduleStub.getBeginningDate(), individualLessonsSchedule.getBeginningDate());
        assertEquals(lessonsScheduleStub.getEndDate(), individualLessonsSchedule.getEndDate());
        assertEquals(lessonsScheduleStub.getScheduleType(), individualLessonsSchedule.getScheduleType());
        assertEquals(lessonsScheduleStub.getAllLessonsDurationInMinutes(), individualLessonsSchedule.getAllLessonsDurationInMinutes());
        assertEquals(lessonsScheduleStub.getDaysOfWeekWithTimes(), individualLessonsSchedule.getDaysOfWeekWithTimes());
        assertEquals(lessonsScheduleStub.getTitles(), individualLessonsSchedule.getTitles());
        assertEquals(lessonsScheduleStub.getOrganizationEntity(), individualLessonsSchedule.getOrganizationEntity());
        assertEquals(lessonsScheduleStub.getTutorEntity(), individualLessonsSchedule.getTutorEntity());
        assertEquals(studentEntityStub, individualLessonsSchedule.getStudentEntity());
    }
}