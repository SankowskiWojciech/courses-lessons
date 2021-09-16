package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonsScheduleRequest;
import com.github.sankowskiwojciech.courseslessons.stub.LessonsScheduleRequestStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonsScheduleRequestAndExternalEntitiesToLessonsScheduleTest {

    @Test
    public void shouldTransformCorrectly() {
        //given
        final LocalDate beginningDateStub = LocalDate.now();
        final LocalDate endDateStub = beginningDateStub.plusMonths(3);
        LessonsScheduleRequest lessonsScheduleRequestStub = LessonsScheduleRequestStub.createWithScheduleTypeFixedDatesLessons(beginningDateStub, endDateStub);
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        StudentEntity studentEntityStub = StudentEntityStub.create();

        //when
        LessonsSchedule lessonsSchedule = LessonsScheduleRequestAndExternalEntitiesToLessonsSchedule.transform(lessonsScheduleRequestStub, organizationEntityStub, tutorEntityStub);

        //then
        assertNotNull(lessonsSchedule);
        assertEquals(lessonsScheduleRequestStub.getBeginningDate(), lessonsSchedule.getBeginningDate());
        assertEquals(lessonsScheduleRequestStub.getEndDate(), lessonsSchedule.getEndDate());
        assertEquals(lessonsScheduleRequestStub.getAllLessonsDurationInMinutes(), lessonsSchedule.getAllLessonsDurationInMinutes());
        assertEquals(lessonsScheduleRequestStub.getDaysOfWeekWithTimes(), lessonsSchedule.getDaysOfWeekWithTimes());
        assertEquals(lessonsScheduleRequestStub.getScheduleType(), lessonsSchedule.getScheduleType());
        assertEquals(lessonsScheduleRequestStub.getTitles(), lessonsSchedule.getTitles());
        assertEquals(organizationEntityStub, lessonsSchedule.getOrganizationEntity());
        assertEquals(tutorEntityStub, lessonsSchedule.getTutorEntity());
    }
}