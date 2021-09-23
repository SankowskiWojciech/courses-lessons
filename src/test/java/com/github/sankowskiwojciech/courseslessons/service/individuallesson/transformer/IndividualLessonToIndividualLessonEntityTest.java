package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IndividualLessonToIndividualLessonEntityTest {

    private final IndividualLessonToIndividualLessonEntity testee = IndividualLessonToIndividualLessonEntity.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        IndividualLesson stub = IndividualLessonStub.createWithExternalEntities(OrganizationEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());

        //when
        IndividualLessonEntity entity = testee.apply(stub);

        //then
        assertNotNull(entity);
        assertEquals(stub.getTitle(), entity.getTitle());
        assertEquals(stub.getStartDate(), entity.getStartDate());
        assertEquals(stub.getEndDate(), entity.getEndDate());
        assertEquals(stub.getDescription(), entity.getDescription());
        assertEquals(stub.getOrganizationEntity(), entity.getOrganizationEntity());
        assertEquals(stub.getTutorEntity(), entity.getTutorEntity());
        assertEquals(stub.getStudentEntity(), entity.getStudentEntity());
        assertNotNull(entity.getCreationDateTime());
    }
}