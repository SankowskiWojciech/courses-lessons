package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonsScheduleRequestStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
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
        IndividualLessonsScheduleRequest individualLessonsScheduleRequestStub = IndividualLessonsScheduleRequestStub.createWithScheduleTypeFixedDatesLessons(beginningDateStub, endDateStub);
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        StudentEntity studentEntityStub = StudentEntityStub.create();

        //when
        IndividualLessonsSchedule individualLessonsSchedule = IndividualLessonsScheduleRequestAndExternalEntitiesToIndividualLessonsSchedule.transform(individualLessonsScheduleRequestStub, organizationEntityStub, tutorEntityStub, studentEntityStub);

        //then
        assertNotNull(individualLessonsSchedule);
        assertEquals(individualLessonsScheduleRequestStub.getBeginningDate(), individualLessonsSchedule.getBeginningDate());
        assertEquals(individualLessonsScheduleRequestStub.getEndDate(), individualLessonsSchedule.getEndDate());
        assertEquals(individualLessonsScheduleRequestStub.getAllLessonsDurationInMinutes(), individualLessonsSchedule.getAllLessonsDurationInMinutes());
        assertEquals(individualLessonsScheduleRequestStub.getDaysOfWeekWithTimes(), individualLessonsSchedule.getDaysOfWeekWithTimes());
        assertEquals(individualLessonsScheduleRequestStub.getScheduleType(), individualLessonsSchedule.getScheduleType());
        assertEquals(individualLessonsScheduleRequestStub.getTitles(), individualLessonsSchedule.getTitles());
        assertEquals(organizationEntityStub, individualLessonsSchedule.getOrganizationEntity());
        assertEquals(tutorEntityStub, individualLessonsSchedule.getTutorEntity());
        assertEquals(studentEntityStub, individualLessonsSchedule.getStudentEntity());
    }
}