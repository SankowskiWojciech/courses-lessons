package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.subdomain.SubdomainEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonsScheduleRequest;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonsScheduleRequestStub;
import com.github.sankowskiwojciech.coursestestlib.stub.SubdomainEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.TutorEntityStub;
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
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();

        //when
        LessonsSchedule schedule = LessonsScheduleRequestAndExternalEntitiesToLessonsSchedule.transform(requestStub, subdomainStub, tutorStub);

        //then
        assertNotNull(schedule);
        assertEquals(requestStub.getBeginningDate(), schedule.getBeginningDate());
        assertEquals(requestStub.getEndDate(), schedule.getEndDate());
        assertEquals(requestStub.getDurationOfAllLessonsInMinutes(), schedule.getAllLessonsDurationInMinutes());
        assertEquals(requestStub.getDaysOfWeekWithTimes(), schedule.getDaysOfWeekWithTimes());
        assertEquals(requestStub.getScheduleType(), schedule.getScheduleType());
        assertEquals(requestStub.getTitles(), schedule.getTitles());
        assertEquals(subdomainStub, schedule.getSubdomainEntity());
        assertEquals(tutorStub, schedule.getTutorEntity());
    }
}