package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonStub;
import com.github.sankowskiwojciech.coursestestlib.stub.StudentEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.SubdomainEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.TutorEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IndividualLessonToIndividualLessonEntityTest {
    private final IndividualLessonToIndividualLessonEntity testee = IndividualLessonToIndividualLessonEntity.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        IndividualLesson stub = IndividualLessonStub.createWithExternalEntities(SubdomainEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());

        //when
        IndividualLessonEntity entity = testee.apply(stub);

        //then
        assertNotNull(entity);
        assertEquals(stub.getTitle(), entity.getTitle());
        assertEquals(stub.getStartDate(), entity.getStartDate());
        assertEquals(stub.getEndDate(), entity.getEndDate());
        assertEquals(stub.getDescription(), entity.getDescription());
        assertEquals(stub.getSubdomainEntity(), entity.getSubdomainEntity());
        assertEquals(stub.getTutorEntity(), entity.getTutorEntity());
        assertEquals(stub.getStudentEntity(), entity.getStudentEntity());
        assertNotNull(entity.getCreationDateTime());
    }
}