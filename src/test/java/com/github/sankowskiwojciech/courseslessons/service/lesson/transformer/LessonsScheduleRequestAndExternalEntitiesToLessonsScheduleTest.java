package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonsScheduleRequest;
import com.github.sankowskiwojciech.courseslessons.stub.LessonsScheduleRequestStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
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
        LessonsScheduleRequest requestStub = LessonsScheduleRequestStub.createWithScheduleTypeFixedDatesLessons(beginningDateStub, endDateStub);
        OrganizationEntity organizationStub = OrganizationEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();

        //when
        LessonsSchedule schedule = LessonsScheduleRequestAndExternalEntitiesToLessonsSchedule.transform(requestStub, organizationStub, tutorStub);

        //then
        assertNotNull(schedule);
        assertEquals(requestStub.getBeginningDate(), schedule.getBeginningDate());
        assertEquals(requestStub.getEndDate(), schedule.getEndDate());
        assertEquals(requestStub.getAllLessonsDurationInMinutes(), schedule.getAllLessonsDurationInMinutes());
        assertEquals(requestStub.getDaysOfWeekWithTimes(), schedule.getDaysOfWeekWithTimes());
        assertEquals(requestStub.getScheduleType(), schedule.getScheduleType());
        assertEquals(requestStub.getTitles(), schedule.getTitles());
        assertEquals(organizationStub, schedule.getOrganizationEntity());
        assertEquals(tutorStub, schedule.getTutorEntity());
    }
}