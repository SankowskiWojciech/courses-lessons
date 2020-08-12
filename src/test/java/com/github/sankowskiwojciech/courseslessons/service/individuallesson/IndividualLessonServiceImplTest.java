package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.courseslessons.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class IndividualLessonServiceImplTest {

    private final IndividualLessonRepository individualLessonRepositoryMock = mock(IndividualLessonRepository.class);
    private final IndividualLessonService testee = new IndividualLessonServiceImpl(individualLessonRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(individualLessonRepositoryMock);
    }

    @Test
    public void shouldCreateIndividualLessonCorrectly() {
        //given
        IndividualLesson individualLessonStub = IndividualLessonStub.createWithExternalEntities(OrganizationEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());

        //when
        IndividualLessonResponse individualLessonResponse = testee.createIndividualLesson(individualLessonStub);

        //then
        verify(individualLessonRepositoryMock).save(any(IndividualLessonEntity.class));

        assertNotNull(individualLessonResponse);
        assertEquals(individualLessonStub.getTitle(), individualLessonResponse.getTitle());
        assertEquals(individualLessonStub.getDateOfLesson(), individualLessonResponse.getDateOfLesson());
        assertEquals(individualLessonStub.getDescription(), individualLessonResponse.getDescription());
        assertEquals(individualLessonStub.getOrganizationEntity().getAlias(), individualLessonResponse.getSubdomainName());
        assertEquals(individualLessonStub.getTutorEntity().getEmailAddress(), individualLessonResponse.getTutorId());
        assertEquals(individualLessonStub.getStudentEntity().getEmailAddress(), individualLessonResponse.getStudentId());
    }
}