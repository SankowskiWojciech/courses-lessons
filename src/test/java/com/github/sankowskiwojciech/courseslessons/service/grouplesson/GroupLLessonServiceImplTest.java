package com.github.sankowskiwojciech.courseslessons.service.grouplesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.service.lesson.file.LessonFileService;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupLessonEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupLessonStub;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonFileEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonFileWithoutContentStub;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.FILES_IDS_STUB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class GroupLLessonServiceImplTest {
    private final LessonFileService lessonFileServiceMock = mock(LessonFileService.class);
    private final FileRepository fileRepositoryMock = mock(FileRepository.class);
    private final GroupLessonRepository groupLessonRepositoryMock = mock(GroupLessonRepository.class);
    private final GroupLessonService testee = new GroupLessonServiceImpl(groupLessonRepositoryMock, fileRepositoryMock, lessonFileServiceMock);

    @Before
    public void reset() {
        Mockito.reset(groupLessonRepositoryMock, lessonFileServiceMock, fileRepositoryMock);
    }

    @Test
    public void shouldCreateIndividualLessonWithFilesCorrectly() {
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
        verify(fileRepositoryMock).findAllByIdIn(anySet());

        assertGroupLessonResponse(entityStub.getId(), lessonStub, response);
        assertLessonFileResponses(filesWithoutContent, response.getFilesInformation());
    }

    @Test
    public void shouldCreateIndividualLessonWithoutFilesCorrectly() {
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
        assertGroupLessonResponse(entityStub.getId(), lessonStub, response);
        assertNotNull(response.getFilesInformation());
        assertTrue(response.getFilesInformation().isEmpty());
    }

    private void assertGroupLessonResponse(String id, GroupLesson lessonStub, GroupLessonResponse response) {
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(lessonStub.getTitle(), response.getTitle());
        assertEquals(lessonStub.getStartDate(), response.getStartDate());
        assertEquals(lessonStub.getEndDate(), response.getEndDate());
        assertEquals(lessonStub.getDescription(), response.getDescription());
        assertEquals(lessonStub.getSubdomainEntity().getSubdomainId(), response.getSubdomainAlias());
        assertEquals(lessonStub.getTutorEntity().getEmailAddress(), response.getTutorEmailAddress());
        assertEquals(lessonStub.getGroupEntity().getName(), response.getGroupName());
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