package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequestParams;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonFileEntitiesForIterableOfIndividualLessonEntityProvider;
import com.github.sankowskiwojciech.courseslessons.stub.AccountInfoStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonFileEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonRequestParamsStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.INDIVIDUAL_LESSON_ID_STUB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndividualLessonServiceImplTest {

    private final IndividualLessonRepository individualLessonRepositoryMock = mock(IndividualLessonRepository.class);
    private final IndividualLessonFileRepository individualLessonFileRepositoryMock = mock(IndividualLessonFileRepository.class);
    private final IndividualLessonFileEntitiesForIterableOfIndividualLessonEntityProvider individualLessonFileEntitiesForIterableOfIndividualLessonEntityProviderMock = mock(IndividualLessonFileEntitiesForIterableOfIndividualLessonEntityProvider.class);
    private final IndividualLessonService testee = new IndividualLessonServiceImpl(individualLessonRepositoryMock, individualLessonFileRepositoryMock, individualLessonFileEntitiesForIterableOfIndividualLessonEntityProviderMock);

    @Before
    public void reset() {
        Mockito.reset(individualLessonRepositoryMock, individualLessonFileRepositoryMock, individualLessonFileEntitiesForIterableOfIndividualLessonEntityProviderMock);
    }

    @Test
    public void shouldCreateIndividualLessonCorrectly() {
        //given
        IndividualLesson individualLessonStub = IndividualLessonStub.createWithExternalEntities(OrganizationEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
        IndividualLessonEntity individualLessonEntityStub = IndividualLessonEntityStub.create(INDIVIDUAL_LESSON_ID_STUB);
        List<IndividualLessonFileEntity> individualLessonFileEntitiesStub = Lists.newArrayList(
                IndividualLessonFileEntityStub.create(individualLessonEntityStub.getLessonId(), 1L),
                IndividualLessonFileEntityStub.create(individualLessonEntityStub.getLessonId(), 2L),
                IndividualLessonFileEntityStub.create(individualLessonEntityStub.getLessonId(), 3L)
        );

        when(individualLessonRepositoryMock.save(any(IndividualLessonEntity.class))).thenReturn(individualLessonEntityStub);
        when(individualLessonFileRepositoryMock.saveAll(anyList())).thenReturn(individualLessonFileEntitiesStub);

        //when
        IndividualLessonResponse individualLessonResponse = testee.createIndividualLesson(individualLessonStub);

        //then
        verify(individualLessonRepositoryMock).save(any(IndividualLessonEntity.class));
        verify(individualLessonFileRepositoryMock).saveAll(anyList());

        assertNotNull(individualLessonResponse);
        assertEquals(individualLessonStub.getTitle(), individualLessonResponse.getTitle());
        assertEquals(individualLessonStub.getStartDateOfLesson(), individualLessonResponse.getStartDateOfLesson());
        assertEquals(individualLessonStub.getEndDateOfLesson(), individualLessonResponse.getEndDateOfLesson());
        assertEquals(individualLessonStub.getDescription(), individualLessonResponse.getDescription());
        assertEquals(individualLessonStub.getOrganizationEntity().getAlias(), individualLessonResponse.getSubdomainName());
        assertEquals(individualLessonStub.getTutorEntity().getEmailAddress(), individualLessonResponse.getTutorEmailAddress());
        assertNotNull(individualLessonResponse.getStudentFullName());
        assertEquals(individualLessonStub.getStudentEntity().getEmailAddress(), individualLessonResponse.getStudentEmailAddress());
        assertEquals(individualLessonStub.getFilesIds(), individualLessonResponse.getFilesIds());
    }

    @Test
    public void shouldReadIndividualLessonsCorrectly() {
        //given
        AccountInfo accountInfoStub = AccountInfoStub.create();
        IndividualLessonRequestParams individualLessonRequestParamsStub = IndividualLessonRequestParamsStub.create();
        IndividualLessonEntity individualLessonEntityStub = IndividualLessonEntityStub.createWithDatesOfLesson(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
        Iterable<IndividualLessonEntity> individualLessonEntitiesStub = Lists.newArrayList(individualLessonEntityStub);

        when(individualLessonRepositoryMock.findAll(any(BooleanExpression.class))).thenReturn(individualLessonEntitiesStub);
        when(individualLessonFileEntitiesForIterableOfIndividualLessonEntityProviderMock.apply(eq(individualLessonEntitiesStub))).thenReturn(Collections.emptyMap());

        //when
        List<IndividualLessonResponse> individualLessonResponseList = testee.readIndividualLessons(accountInfoStub, individualLessonRequestParamsStub);

        //then
        verify(individualLessonRepositoryMock).findAll(any(BooleanExpression.class));
        verify(individualLessonFileEntitiesForIterableOfIndividualLessonEntityProviderMock).apply(eq(individualLessonEntitiesStub));

        assertNotNull(individualLessonResponseList);
        assertEquals(1, individualLessonResponseList.size());
        IndividualLessonResponse individualLessonResponse = individualLessonResponseList.stream().findFirst().get();
        assertEquals(individualLessonEntityStub.getTitle(), individualLessonResponse.getTitle());
        assertEquals(individualLessonEntityStub.getStartDateOfLesson(), individualLessonResponse.getStartDateOfLesson());
        assertEquals(individualLessonEntityStub.getEndDateOfLesson(), individualLessonResponse.getEndDateOfLesson());
        assertEquals(individualLessonEntityStub.getDescription(), individualLessonResponse.getDescription());
        assertEquals(individualLessonEntityStub.getOrganizationEntity().getAlias(), individualLessonResponse.getSubdomainName());
        assertEquals(individualLessonEntityStub.getTutorEntity().getEmailAddress(), individualLessonResponse.getTutorEmailAddress());
        assertNotNull(individualLessonResponse.getStudentFullName());
        assertEquals(individualLessonEntityStub.getStudentEntity().getEmailAddress(), individualLessonResponse.getStudentEmailAddress());
        assertTrue(individualLessonResponse.getFilesIds().isEmpty());
    }
}