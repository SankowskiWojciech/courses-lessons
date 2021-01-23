package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.*;
import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequestParams;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider;
import com.github.sankowskiwojciech.courseslessons.stub.*;
import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.INDIVIDUAL_LESSON_ID_STUB;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class IndividualLessonServiceImplTest {

    private final IndividualLessonRepository individualLessonRepositoryMock = mock(IndividualLessonRepository.class);
    private final IndividualLessonFileRepository individualLessonFileRepositoryMock = mock(IndividualLessonFileRepository.class);
    private final LessonFileRepository lessonFileRepositoryMock = mock(LessonFileRepository.class);
    private final IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProviderMock = mock(IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider.class);
    private final IndividualLessonService testee = new IndividualLessonServiceImpl(individualLessonRepositoryMock, individualLessonFileRepositoryMock, lessonFileRepositoryMock, individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProviderMock);

    @Before
    public void reset() {
        Mockito.reset(individualLessonRepositoryMock, individualLessonFileRepositoryMock, individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProviderMock, lessonFileRepositoryMock);
    }

    @Test
    public void shouldCreateIndividualLessonCorrectly() {
        //given
        IndividualLesson individualLessonStub = IndividualLessonStub.createWithExternalEntities(OrganizationEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
        IndividualLessonEntity individualLessonEntityStub = IndividualLessonEntityStub.create(INDIVIDUAL_LESSON_ID_STUB);
        List<IndividualLessonFileEntity> individualLessonFileEntitiesStub = Lists.newArrayList(
                IndividualLessonFileEntityStub.create(individualLessonEntityStub.getLessonId(), 1L)
        );
        List<LessonFileWithoutContent> lessonFilesWithoutContent = Lists.newArrayList(LessonFileWithoutContentStub.createWithFileId(1));

        when(individualLessonRepositoryMock.save(any(IndividualLessonEntity.class))).thenReturn(individualLessonEntityStub);
        when(individualLessonFileRepositoryMock.saveAll(anyList())).thenReturn(individualLessonFileEntitiesStub);
        when(lessonFileRepositoryMock.findAllByFileIdIn(anySet())).thenReturn(lessonFilesWithoutContent);

        //when
        IndividualLessonResponse individualLessonResponse = testee.createIndividualLesson(individualLessonStub);

        //then
        verify(individualLessonRepositoryMock).save(any(IndividualLessonEntity.class));
        verify(individualLessonFileRepositoryMock).saveAll(anyList());
        verify(lessonFileRepositoryMock).findAllByFileIdIn(anySet());

        assertNotNull(individualLessonResponse);
        assertEquals(individualLessonStub.getTitle(), individualLessonResponse.getTitle());
        assertEquals(individualLessonStub.getStartDateOfLesson(), individualLessonResponse.getStartDateOfLesson());
        assertEquals(individualLessonStub.getEndDateOfLesson(), individualLessonResponse.getEndDateOfLesson());
        assertEquals(individualLessonStub.getDescription(), individualLessonResponse.getDescription());
        assertEquals(individualLessonStub.getOrganizationEntity().getAlias(), individualLessonResponse.getSubdomainName());
        assertEquals(individualLessonStub.getTutorEntity().getEmailAddress(), individualLessonResponse.getTutorEmailAddress());
        assertNotNull(individualLessonResponse.getStudentFullName());
        assertEquals(individualLessonStub.getStudentEntity().getEmailAddress(), individualLessonResponse.getStudentEmailAddress());
        assertEquals(individualLessonStub.getFilesIds().size(), individualLessonResponse.getFilesInformation().size());
    }

    @Test
    public void shouldReadIndividualLessonsCorrectly() {
        //given
        AccountInfo accountInfoStub = AccountInfoStub.create();
        LessonRequestParams lessonRequestParamsStub = LessonRequestParamsStub.create();
        IndividualLessonEntity individualLessonEntityStub = IndividualLessonEntityStub.createWithDatesOfLesson(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
        Iterable<IndividualLessonEntity> individualLessonEntitiesStub = Lists.newArrayList(individualLessonEntityStub);

        when(individualLessonRepositoryMock.findAll(any(BooleanExpression.class))).thenReturn(individualLessonEntitiesStub);
        when(individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProviderMock.apply(eq(individualLessonEntitiesStub))).thenReturn(Collections.emptyMap());

        //when
        List<IndividualLessonResponse> individualLessonResponseList = testee.readIndividualLessons(accountInfoStub, lessonRequestParamsStub);

        //then
        verify(individualLessonRepositoryMock).findAll(any(BooleanExpression.class));
        verify(individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProviderMock).apply(eq(individualLessonEntitiesStub));

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
        assertTrue(individualLessonResponse.getFilesInformation().isEmpty());
    }
}