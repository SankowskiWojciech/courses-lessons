package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.subdomain.SubdomainEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonsScheduleRequestStub;
import com.github.sankowskiwojciech.coursestestlib.stub.StudentEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.SubdomainEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.TutorEntityStub;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IndividualLessonsScheduleRequestAndExternalEntitiesToIndividualLessonsScheduleTest {
    @Test
    public void shouldTransformCorrectly() {
        //given
        final LocalDate beginningDateStub = LocalDate.now();
        final LocalDate endDateStub = beginningDateStub.plusMonths(3);
        IndividualLessonsScheduleRequest requestStub = IndividualLessonsScheduleRequestStub.createWithScheduleTypeFixedDatesLessons(beginningDateStub, endDateStub);
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        StudentEntity studentStub = StudentEntityStub.create();

        //when
        IndividualLessonsSchedule schedule = IndividualLessonsScheduleRequestAndExternalEntitiesToIndividualLessonsSchedule.transform(requestStub, subdomainStub, tutorStub, studentStub);

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
        assertEquals(studentStub, schedule.getStudentEntity());
    }
}