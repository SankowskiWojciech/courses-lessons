package com.github.sankowskiwojciech.courseslessons.service.grouplesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequestParams;
import com.github.sankowskiwojciech.courseslessons.service.file.FilePermissionsService;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.GroupLessonsQueryProvider;
import com.github.sankowskiwojciech.courseslessons.service.lesson.file.LessonFileService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonsIdsAndListOfFilesWithoutContentProvider;
import com.github.sankowskiwojciech.coursestestlib.stub.AccountInfoStub;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupLessonEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupLessonStub;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonFileEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonFileWithoutContentStub;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonRequestParamsStub;
import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.FILES_IDS_STUB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class GroupLLessonServiceImplTest {
    private final LessonFileService lessonFileServiceMock = mock(LessonFileService.class);
    private final FileRepository fileRepositoryMock = mock(FileRepository.class);
    private final GroupLessonRepository groupLessonRepositoryMock = mock(GroupLessonRepository.class);
    private final LessonsIdsAndListOfFilesWithoutContentProvider lessonsIdsAndListOfFilesWithoutContentProviderMock = mock(LessonsIdsAndListOfFilesWithoutContentProvider.class);
    private final GroupLessonsQueryProvider groupLessonsQueryProviderMock = mock(GroupLessonsQueryProvider.class);
    private final FilePermissionsService filePermissionsServiceMock = mock(FilePermissionsService.class);
    private final GroupLessonService testee = new GroupLessonServiceImpl(groupLessonRepositoryMock, fileRepositoryMock, lessonFileServiceMock, lessonsIdsAndListOfFilesWithoutContentProviderMock, groupLessonsQueryProviderMock, filePermissionsServiceMock);

    @Before
    public void reset() {
        Mockito.reset(groupLessonRepositoryMock, fileRepositoryMock, lessonFileServiceMock, lessonsIdsAndListOfFilesWithoutContentProviderMock, groupLessonsQueryProviderMock, filePermissionsServiceMock);
    }

    @Test
    public void shouldCreateGroupLessonWithFilesCorrectly() {
        //given
        GroupLesson lessonStub = GroupLessonStub.createWithFilesIds(FILES_IDS_STUB);
        GroupLessonEntity entityStub = GroupLessonEntityStub.create();
        List<LessonFileAccessEntity> lessonFileAccessStub = Lists.newArrayList(
                IndividualLessonFileEntityStub.create(entityStub.getId(), UUID.randomUUID().toString())
        );
        List<FileWithoutContent> filesWithoutContent = Lists.newArrayList(LessonFileWithoutContentStub.createWithFileId(UUID.randomUUID().toString()));

        when(groupLessonRepositoryMock.save(any(GroupLessonEntity.class))).thenReturn(entityStub);
        when(lessonFileServiceMock.attachFilesToLesson(entityStub.getId(), lessonStub.getFilesIds())).thenReturn(lessonFileAccessStub);
        when(fileRepositoryMock.findAllByIdIn(anySet())).thenReturn(filesWithoutContent);

        //when
        GroupLessonResponse response = testee.createGroupLesson(lessonStub);

        //then
        verify(groupLessonRepositoryMock).save(any(GroupLessonEntity.class));
        verify(lessonFileServiceMock).attachFilesToLesson(entityStub.getId(), lessonStub.getFilesIds());
        verify(filePermissionsServiceMock).addUserPermissionsToFilesToStudentsFromGroup(anyString(), anyCollection());
        verify(fileRepositoryMock).findAllByIdIn(anySet());

        assertGroupLessonResponse(entityStub, response);
        assertLessonFileResponses(filesWithoutContent, response.getFilesInformation());
    }

    @Test
    public void shouldCreateGroupLessonWithoutFilesCorrectly() {
        //given
        GroupLesson lessonStub = GroupLessonStub.createWithFilesIds(null);
        GroupLessonEntity entityStub = GroupLessonEntityStub.create();

        when(groupLessonRepositoryMock.save(any(GroupLessonEntity.class))).thenReturn(entityStub);
        when(lessonFileServiceMock.attachFilesToLesson(entityStub.getId(), lessonStub.getFilesIds())).thenReturn(Collections.emptyList());

        //when
        GroupLessonResponse response = testee.createGroupLesson(lessonStub);

        //then
        verify(groupLessonRepositoryMock).save(any(GroupLessonEntity.class));
        verify(lessonFileServiceMock).attachFilesToLesson(entityStub.getId(), lessonStub.getFilesIds());
        verifyNoInteractions(fileRepositoryMock);
        assertGroupLessonResponse(entityStub, response);
        assertNotNull(response.getFilesInformation());
        assertTrue(response.getFilesInformation().isEmpty());
    }

    @Test
    public void shouldReadGroupLessonsCorrectly() {
        //given
        BooleanExpression booleanExpressionMock = mock(BooleanExpression.class);
        AccountInfo accountInfoStub = AccountInfoStub.create();
        LessonRequestParams requestParamsStub = LessonRequestParamsStub.create();
        List<GroupLessonEntity> entitiesStub = Lists.newArrayList(GroupLessonEntityStub.createWithDatesOfLesson(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1)));
        List<String> idsStub = Lists.newArrayList(entitiesStub.get(0).getId());

        when(groupLessonsQueryProviderMock.apply(accountInfoStub, requestParamsStub)).thenReturn(booleanExpressionMock);
        when(groupLessonRepositoryMock.findAll(booleanExpressionMock)).thenReturn(entitiesStub);
        when(lessonsIdsAndListOfFilesWithoutContentProviderMock.apply(idsStub)).thenReturn(Collections.emptyMap());

        //when
        List<GroupLessonResponse> responseList = testee.readGroupLessons(accountInfoStub, requestParamsStub);

        //then
        verify(groupLessonRepositoryMock).findAll(booleanExpressionMock);
        verify(lessonsIdsAndListOfFilesWithoutContentProviderMock).apply(idsStub);

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        GroupLessonResponse response = responseList.stream().findFirst().get();
        GroupLessonEntity entity = entitiesStub.get(0);
        assertEquals(entity.getTitle(), response.getTitle());
        assertEquals(entity.getStartDate(), response.getStartDate());
        assertEquals(entity.getEndDate(), response.getEndDate());
        assertEquals(entity.getDescription(), response.getDescription());
        assertEquals(entity.getSubdomainEntity().getSubdomainId(), response.getSubdomainAlias());
        assertEquals(entity.getTutorEntity().getEmailAddress(), response.getTutorEmailAddress());
        assertNotNull(response.getGroupName());
        assertEquals(entity.getGroupEntity().getName(), response.getGroupName());
        assertTrue(response.getFilesInformation().isEmpty());
    }

    private void assertGroupLessonResponse(GroupLessonEntity lessonEntityStub, GroupLessonResponse response) {
        assertNotNull(response);
        assertEquals(lessonEntityStub.getId(), response.getId());
        assertEquals(lessonEntityStub.getTitle(), response.getTitle());
        assertEquals(lessonEntityStub.getStartDate(), response.getStartDate());
        assertEquals(lessonEntityStub.getEndDate(), response.getEndDate());
        assertEquals(lessonEntityStub.getDescription(), response.getDescription());
        assertEquals(lessonEntityStub.getSubdomainEntity().getSubdomainId(), response.getSubdomainAlias());
        assertEquals(lessonEntityStub.getTutorEntity().getEmailAddress(), response.getTutorEmailAddress());
        assertEquals(lessonEntityStub.getGroupEntity().getName(), response.getGroupName());
    }

    private void assertLessonFileResponses(List<FileWithoutContent> filesWithoutContent, List<LessonFileResponse> lessonFilesResponse) {
        assertEquals(filesWithoutContent.size(), lessonFilesResponse.size());
        FileWithoutContent fileWithoutContent = filesWithoutContent.get(0);
        LessonFileResponse lessonFileResponse = lessonFilesResponse.get(0);
        assertEquals(fileWithoutContent.getId(), lessonFileResponse.getId());
        assertEquals(fileWithoutContent.getName(), lessonFileResponse.getName());
        assertEquals(fileWithoutContent.getCreatedBy(), lessonFileResponse.getCreatedBy());
        assertEquals(fileWithoutContent.getCreationDateTime(), lessonFileResponse.getCreationDateTime());
        assertEquals(fileWithoutContent.getExtension(), lessonFileResponse.getExtension());
    }
}