package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonFileEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IndividualLessonEntityAndIndividualLessonFileEntitiesToIndividualLessonResponseTest {

    private final IndividualLessonEntityAndIndividualLessonFileEntitiesToIndividualLessonResponse testee = IndividualLessonEntityAndIndividualLessonFileEntitiesToIndividualLessonResponse.getInstance();

    @Test
    public void shouldTransformCorrectlyWhenSubdomainIsOrganization() {
        //given
        IndividualLessonEntity individualLessonEntityStub = IndividualLessonEntityStub.createWithExternalEntities(OrganizationEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
        List<IndividualLessonFileEntity> individualLessonFileEntitiesStub = Lists.newArrayList(IndividualLessonFileEntityStub.create(individualLessonEntityStub.getLessonId(), 1L), IndividualLessonFileEntityStub.create(individualLessonEntityStub.getLessonId(), 2L));

        //when
        IndividualLessonResponse individualLessonResponse = testee.apply(individualLessonEntityStub, individualLessonFileEntitiesStub);

        //then
        assertNotNull(individualLessonResponse);
        assertEquals(individualLessonEntityStub.getLessonId(), individualLessonResponse.getLessonId());
        assertEquals(individualLessonEntityStub.getTitle(), individualLessonResponse.getTitle());
        assertEquals(individualLessonEntityStub.getStartDateOfLesson(), individualLessonResponse.getStartDateOfLesson());
        assertEquals(individualLessonEntityStub.getEndDateOfLesson(), individualLessonResponse.getEndDateOfLesson());
        assertEquals(individualLessonEntityStub.getDescription(), individualLessonResponse.getDescription());
        assertEquals(individualLessonEntityStub.getOrganizationEntity().getAlias(), individualLessonResponse.getSubdomainName());
        assertEquals(individualLessonEntityStub.getTutorEntity().getEmailAddress(), individualLessonResponse.getTutorEmailAddress());
        assertNotNull(individualLessonResponse.getStudentFullName());
        assertEquals(individualLessonEntityStub.getStudentEntity().getEmailAddress(), individualLessonResponse.getStudentEmailAddress());
        assertEquals(individualLessonFileEntitiesStub.size(), individualLessonResponse.getFilesIds().size());
    }

    @Test
    public void shouldTransformCorrectlyWhenSubdomainIsTutor() {
        //given
        IndividualLessonEntity individualLessonEntityStub = IndividualLessonEntityStub.createWithExternalEntities(null, TutorEntityStub.create(), StudentEntityStub.create());
        List<IndividualLessonFileEntity> individualLessonFileEntitiesStub = Lists.newArrayList(IndividualLessonFileEntityStub.create(individualLessonEntityStub.getLessonId(), 1L), IndividualLessonFileEntityStub.create(individualLessonEntityStub.getLessonId(), 2L));

        //when
        IndividualLessonResponse individualLessonResponse = testee.apply(individualLessonEntityStub, individualLessonFileEntitiesStub);

        //then
        assertNotNull(individualLessonResponse);
        assertEquals(individualLessonEntityStub.getLessonId(), individualLessonResponse.getLessonId());
        assertEquals(individualLessonEntityStub.getTitle(), individualLessonResponse.getTitle());
        assertEquals(individualLessonEntityStub.getStartDateOfLesson(), individualLessonResponse.getStartDateOfLesson());
        assertEquals(individualLessonEntityStub.getEndDateOfLesson(), individualLessonResponse.getEndDateOfLesson());
        assertEquals(individualLessonEntityStub.getDescription(), individualLessonResponse.getDescription());
        assertEquals(individualLessonEntityStub.getTutorEntity().getAlias(), individualLessonResponse.getSubdomainName());
        assertEquals(individualLessonEntityStub.getTutorEntity().getEmailAddress(), individualLessonResponse.getTutorEmailAddress());
        assertNotNull(individualLessonResponse.getStudentFullName());
        assertEquals(individualLessonEntityStub.getStudentEntity().getEmailAddress(), individualLessonResponse.getStudentEmailAddress());
        assertEquals(individualLessonFileEntitiesStub.size(), individualLessonResponse.getFilesIds().size());
    }
}