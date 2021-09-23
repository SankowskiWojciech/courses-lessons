package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequestParams;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider;
import com.github.sankowskiwojciech.courseslessons.stub.AccountInfoStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonFileEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileWithoutContentStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonRequestParamsStub;
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
import java.util.UUID;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.INDIVIDUAL_LESSON_ID_STUB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndividualLessonServiceImplTest {

    private final IndividualLessonRepository individualLessonRepositoryMock = mock(IndividualLessonRepository.class);
    private final LessonFileAccessRepository lessonFileAccessRepositoryMock = mock(LessonFileAccessRepository.class);
    private final FileRepository fileRepositoryMock = mock(FileRepository.class);
    private final IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProviderMock = mock(IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider.class);
    private final IndividualLessonService testee = new IndividualLessonServiceImpl(individualLessonRepositoryMock, lessonFileAccessRepositoryMock, fileRepositoryMock, individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProviderMock);

    @Before
    public void reset() {
        Mockito.reset(individualLessonRepositoryMock, lessonFileAccessRepositoryMock, individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProviderMock, fileRepositoryMock);
    }

    @Test
    public void shouldCreateIndividualLessonCorrectly() {
        //given
        IndividualLesson lessonStub = IndividualLessonStub.createWithExternalEntities(OrganizationEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
        IndividualLessonEntity entityStub = IndividualLessonEntityStub.create(INDIVIDUAL_LESSON_ID_STUB);
        List<LessonFileAccessEntity> lessonFileAccessStub = Lists.newArrayList(
                IndividualLessonFileEntityStub.create(entityStub.getId(), UUID.randomUUID().toString())
        );
        List<FileWithoutContent> filesWithoutContent = Lists.newArrayList(LessonFileWithoutContentStub.createWithFileId(UUID.randomUUID().toString()));

        when(individualLessonRepositoryMock.save(any(IndividualLessonEntity.class))).thenReturn(entityStub);
        when(lessonFileAccessRepositoryMock.saveAll(anyList())).thenReturn(lessonFileAccessStub);
        when(fileRepositoryMock.findAllByIdIn(anySet())).thenReturn(filesWithoutContent);

        //when
        IndividualLessonResponse response = testee.createIndividualLesson(lessonStub);

        //then
        verify(individualLessonRepositoryMock).save(any(IndividualLessonEntity.class));
        verify(lessonFileAccessRepositoryMock).saveAll(anyList());
        verify(fileRepositoryMock).findAllByIdIn(anySet());

        assertNotNull(response);
        assertEquals(lessonStub.getTitle(), response.getTitle());
        assertEquals(lessonStub.getStartDate(), response.getStartDate());
        assertEquals(lessonStub.getEndDate(), response.getEndDate());
        assertEquals(lessonStub.getDescription(), response.getDescription());
        assertEquals(lessonStub.getOrganizationEntity().getAlias(), response.getSubdomainName());
        assertEquals(lessonStub.getTutorEntity().getEmailAddress(), response.getTutorEmailAddress());
        assertNotNull(response.getStudentFullName());
        assertEquals(lessonStub.getStudentEntity().getEmailAddress(), response.getStudentEmailAddress());
        assertEquals(lessonStub.getFilesIds().size(), response.getFilesInformation().size());
    }

    @Test
    public void shouldReadIndividualLessonsCorrectly() {
        //given
        AccountInfo accountInfoStub = AccountInfoStub.create();
        LessonRequestParams requestParamsStub = LessonRequestParamsStub.create();
        IndividualLessonEntity entityStub = IndividualLessonEntityStub.createWithDatesOfLesson(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
        Iterable<IndividualLessonEntity> entitiesStub = Lists.newArrayList(entityStub);

        when(individualLessonRepositoryMock.findAll(any(BooleanExpression.class))).thenReturn(entitiesStub);
        when(individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProviderMock.apply(eq(entitiesStub))).thenReturn(Collections.emptyMap());

        //when
        List<IndividualLessonResponse> responseList = testee.readIndividualLessons(accountInfoStub, requestParamsStub);

        //then
        verify(individualLessonRepositoryMock).findAll(any(BooleanExpression.class));
        verify(individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProviderMock).apply(eq(entitiesStub));

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        IndividualLessonResponse individualLessonResponse = responseList.stream().findFirst().get();
        assertEquals(entityStub.getTitle(), individualLessonResponse.getTitle());
        assertEquals(entityStub.getStartDate(), individualLessonResponse.getStartDate());
        assertEquals(entityStub.getEndDate(), individualLessonResponse.getEndDate());
        assertEquals(entityStub.getDescription(), individualLessonResponse.getDescription());
        assertEquals(entityStub.getOrganizationEntity().getAlias(), individualLessonResponse.getSubdomainName());
        assertEquals(entityStub.getTutorEntity().getEmailAddress(), individualLessonResponse.getTutorEmailAddress());
        assertNotNull(individualLessonResponse.getStudentFullName());
        assertEquals(entityStub.getStudentEntity().getEmailAddress(), individualLessonResponse.getStudentEmailAddress());
        assertTrue(individualLessonResponse.getFilesInformation().isEmpty());
    }
}