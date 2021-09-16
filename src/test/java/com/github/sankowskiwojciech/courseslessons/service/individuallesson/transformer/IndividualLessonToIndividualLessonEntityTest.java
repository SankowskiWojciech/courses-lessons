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
        IndividualLesson individualLessonStub = IndividualLessonStub.createWithExternalEntities(OrganizationEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());

        //when
        IndividualLessonEntity individualLessonEntity = testee.apply(individualLessonStub);

        //then
        assertNotNull(individualLessonEntity);
        assertEquals(individualLessonStub.getTitle(), individualLessonEntity.getTitle());
        assertEquals(individualLessonStub.getStartDate(), individualLessonEntity.getStartDate());
        assertEquals(individualLessonStub.getEndDate(), individualLessonEntity.getEndDate());
        assertEquals(individualLessonStub.getDescription(), individualLessonEntity.getDescription());
        assertEquals(individualLessonStub.getOrganizationEntity(), individualLessonEntity.getOrganizationEntity());
        assertEquals(individualLessonStub.getTutorEntity(), individualLessonEntity.getTutorEntity());
        assertEquals(individualLessonStub.getStudentEntity(), individualLessonEntity.getStudentEntity());
        assertNotNull(individualLessonEntity.getCreationDateTime());
    }
}