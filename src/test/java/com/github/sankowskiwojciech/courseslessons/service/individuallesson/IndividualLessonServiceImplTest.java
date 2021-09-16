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
        IndividualLesson individualLessonStub = IndividualLessonStub.createWithExternalEntities(OrganizationEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
        IndividualLessonEntity individualLessonEntityStub = IndividualLessonEntityStub.create(INDIVIDUAL_LESSON_ID_STUB);
        List<LessonFileAccessEntity> individualLessonFileEntitiesStub = Lists.newArrayList(
                IndividualLessonFileEntityStub.create(individualLessonEntityStub.getId(), UUID.randomUUID().toString())
        );
        List<FileWithoutContent> lessonFilesWithoutContent = Lists.newArrayList(LessonFileWithoutContentStub.createWithFileId(UUID.randomUUID().toString()));

        when(individualLessonRepositoryMock.save(any(IndividualLessonEntity.class))).thenReturn(individualLessonEntityStub);
        when(lessonFileAccessRepositoryMock.saveAll(anyList())).thenReturn(individualLessonFileEntitiesStub);
        when(fileRepositoryMock.findAllByIdIn(anySet())).thenReturn(lessonFilesWithoutContent);

        //when
        IndividualLessonResponse individualLessonResponse = testee.createIndividualLesson(individualLessonStub);

        //then
        verify(individualLessonRepositoryMock).save(any(IndividualLessonEntity.class));
        verify(lessonFileAccessRepositoryMock).saveAll(anyList());
        verify(fileRepositoryMock).findAllByIdIn(anySet());

        assertNotNull(individualLessonResponse);
        assertEquals(individualLessonStub.getTitle(), individualLessonResponse.getTitle());
        assertEquals(individualLessonStub.getStartDate(), individualLessonResponse.getStartDate());
        assertEquals(individualLessonStub.getEndDate(), individualLessonResponse.getEndDate());
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
        assertEquals(individualLessonEntityStub.getStartDate(), individualLessonResponse.getStartDate());
        assertEquals(individualLessonEntityStub.getEndDate(), individualLessonResponse.getEndDate());
        assertEquals(individualLessonEntityStub.getDescription(), individualLessonResponse.getDescription());
        assertEquals(individualLessonEntityStub.getOrganizationEntity().getAlias(), individualLessonResponse.getSubdomainName());
        assertEquals(individualLessonEntityStub.getTutorEntity().getEmailAddress(), individualLessonResponse.getTutorEmailAddress());
        assertNotNull(individualLessonResponse.getStudentFullName());
        assertEquals(individualLessonEntityStub.getStudentEntity().getEmailAddress(), individualLessonResponse.getStudentEmailAddress());
        assertTrue(individualLessonResponse.getFilesInformation().isEmpty());
    }
}