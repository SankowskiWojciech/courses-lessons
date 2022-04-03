package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequestParams;
import com.github.sankowskiwojciech.courseslessons.service.file.FilePermissionsService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.file.LessonFileService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonsIdsAndListOfFilesWithoutContentProvider;
import com.github.sankowskiwojciech.coursestestlib.stub.AccountInfoStub;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonFileEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonStub;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonFileWithoutContentStub;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonRequestParamsStub;
import com.github.sankowskiwojciech.coursestestlib.stub.StudentEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.SubdomainEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.TutorEntityStub;
import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.INDIVIDUAL_LESSON_ID_STUB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndividualLessonServiceImplTest {
    private final IndividualLessonRepository individualLessonRepositoryMock = mock(IndividualLessonRepository.class);
    private final LessonFileService lessonFileServiceMock = mock(LessonFileService.class);
    private final FileRepository fileRepositoryMock = mock(FileRepository.class);
    private final LessonsIdsAndListOfFilesWithoutContentProvider lessonsIdsAndListOfFilesWithoutContentProviderMock = mock(LessonsIdsAndListOfFilesWithoutContentProvider.class);
    private final FilePermissionsService filePermissionsServiceMock = mock(FilePermissionsService.class);
    private final IndividualLessonService testee = new IndividualLessonServiceImpl(individualLessonRepositoryMock, lessonFileServiceMock, fileRepositoryMock, lessonsIdsAndListOfFilesWithoutContentProviderMock, filePermissionsServiceMock);

    @Before
    public void reset() {
        Mockito.reset(individualLessonRepositoryMock, lessonFileServiceMock, lessonsIdsAndListOfFilesWithoutContentProviderMock, fileRepositoryMock, filePermissionsServiceMock);
    }

    @Test
    public void shouldCreateIndividualLessonCorrectly() {
        //given
        IndividualLesson lessonStub = IndividualLessonStub.createWithExternalEntities(SubdomainEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
        IndividualLessonEntity entityStub = IndividualLessonEntityStub.create(INDIVIDUAL_LESSON_ID_STUB);
        List<LessonFileAccessEntity> lessonFileAccessStub = Lists.newArrayList(
                IndividualLessonFileEntityStub.create(entityStub.getId(), UUID.randomUUID().toString())
        );
        List<FileWithoutContent> filesWithoutContent = Lists.newArrayList(LessonFileWithoutContentStub.createWithFileId(UUID.randomUUID().toString()));

        when(individualLessonRepositoryMock.save(any(IndividualLessonEntity.class))).thenReturn(entityStub);
        when(lessonFileServiceMock.attachFilesToLesson(entityStub.getId(), lessonStub.getFilesIds())).thenReturn(lessonFileAccessStub);
        when(fileRepositoryMock.findAllByIdIn(anySet())).thenReturn(filesWithoutContent);

        //when
        IndividualLessonResponse response = testee.createIndividualLesson(lessonStub);

        //then
        verify(individualLessonRepositoryMock).save(any(IndividualLessonEntity.class));
        verify(lessonFileServiceMock).attachFilesToLesson(entityStub.getId(), lessonStub.getFilesIds());
        verify(filePermissionsServiceMock).addUserPermissionsToFiles(anyString(), anyCollection());
        verify(fileRepositoryMock).findAllByIdIn(anySet());

        assertNotNull(response);
        assertEquals(entityStub.getTitle(), response.getTitle());
        assertEquals(entityStub.getStartDate(), response.getStartDate());
        assertEquals(entityStub.getEndDate(), response.getEndDate());
        assertEquals(entityStub.getDescription(), response.getDescription());
        assertEquals(entityStub.getSubdomainEntity().getSubdomainId(), response.getSubdomainAlias());
        assertEquals(entityStub.getTutorEntity().getEmailAddress(), response.getTutorEmailAddress());
        assertNotNull(response.getStudentFullName());
        assertEquals(entityStub.getStudentEntity().getEmailAddress(), response.getStudentEmailAddress());
        assertEquals(lessonStub.getFilesIds().size(), response.getFilesInformation().size());
    }

    @Test
    public void shouldReadIndividualLessonsCorrectly() {
        //given
        AccountInfo accountInfoStub = AccountInfoStub.create();
        LessonRequestParams requestParamsStub = LessonRequestParamsStub.create();
        List<IndividualLessonEntity> entitiesStub = Lists.newArrayList(IndividualLessonEntityStub.createWithDatesOfLesson(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1)));
        List<String> idsStub = Lists.newArrayList(entitiesStub.get(0).getId());

        when(individualLessonRepositoryMock.findAll(any(BooleanExpression.class))).thenReturn(entitiesStub);
        when(lessonsIdsAndListOfFilesWithoutContentProviderMock.apply(idsStub)).thenReturn(Collections.emptyMap());

        //when
        List<IndividualLessonResponse> responseList = testee.readIndividualLessons(accountInfoStub, requestParamsStub);

        //then
        verify(individualLessonRepositoryMock).findAll(any(BooleanExpression.class));
        verify(lessonsIdsAndListOfFilesWithoutContentProviderMock).apply(idsStub);

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        IndividualLessonResponse response = responseList.stream().findFirst().get();
        IndividualLessonEntity entity = entitiesStub.get(0);
        assertEquals(entity.getTitle(), response.getTitle());
        assertEquals(entity.getStartDate(), response.getStartDate());
        assertEquals(entity.getEndDate(), response.getEndDate());
        assertEquals(entity.getDescription(), response.getDescription());
        assertEquals(entity.getSubdomainEntity().getSubdomainId(), response.getSubdomainAlias());
        assertEquals(entity.getTutorEntity().getEmailAddress(), response.getTutorEmailAddress());
        assertNotNull(response.getStudentFullName());
        assertEquals(entity.getStudentEntity().getEmailAddress(), response.getStudentEmailAddress());
        assertTrue(response.getFilesInformation().isEmpty());
    }
}