package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequest;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonRequestStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IndividualLessonRequestAndExternalEntitiesToIndividualLessonTest {

    @Test
    public void shouldTransformCorrectly() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        StudentEntity studentEntityStub = StudentEntityStub.create();

        //when
        IndividualLesson individualLesson = IndividualLessonRequestAndExternalEntitiesToIndividualLesson.transform(individualLessonRequestStub, organizationEntityStub, tutorEntityStub, studentEntityStub);

        //then
        assertNotNull(individualLesson);
        assertEquals(individualLessonRequestStub.getTitle(), individualLesson.getTitle());
        assertEquals(individualLessonRequestStub.getStartDateOfLesson(), individualLesson.getStartDateOfLesson());
        assertEquals(individualLessonRequestStub.getEndDateOfLesson(), individualLesson.getEndDateOfLesson());
        assertEquals(individualLessonRequestStub.getDescription(), individualLesson.getDescription());
        assertEquals(organizationEntityStub, individualLesson.getOrganizationEntity());
        assertEquals(tutorEntityStub, individualLesson.getTutorEntity());
        assertEquals(studentEntityStub, individualLesson.getStudentEntity());
    }
}