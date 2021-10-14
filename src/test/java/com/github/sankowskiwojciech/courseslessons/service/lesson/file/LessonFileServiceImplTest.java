package com.github.sankowskiwojciech.courseslessons.service.lesson.file;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileAccessEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileWithoutContentStub;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILES_IDS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.INDIVIDUAL_LESSON_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LessonFileServiceImplTest {

    private final FileRepository fileRepositoryMock = mock(FileRepository.class);
    private final LessonFileAccessRepository lessonFileAccessRepositoryMock = mock(LessonFileAccessRepository.class);
    private final LessonFileService testee = new LessonFileServiceImpl(fileRepositoryMock, lessonFileAccessRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(fileRepositoryMock, lessonFileAccessRepositoryMock);
    }

    @Test
    public void shouldCreateLessonFileCorrectly() {
        //given
        LessonFile fileStub = LessonFileStub.create();
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        FileEntity entityStub = LessonFileEntityStub.create();

        when(fileRepositoryMock.save(any(FileEntity.class))).thenReturn(entityStub);

        //when
        LessonFileResponse response = testee.createLessonFile(fileStub, userIdStub);

        //then
        verify(fileRepositoryMock).save(any(FileEntity.class));

        assertNotNull(response);
        assertEquals(entityStub.getId(), response.getId());
        assertEquals(entityStub.getName(), response.getName());
        assertEquals(entityStub.getExtension(), response.getExtension());
        assertEquals(entityStub.getCreatedBy(), response.getCreatedBy());
        assertEquals(entityStub.getCreationDateTime(), response.getCreationDateTime());
    }

    @Test
    public void shouldReadFileCorrectly() {
        //given
        String fileId = FILE_ID_STUB;
        FileEntity entityStub = LessonFileEntityStub.create();

        when(fileRepositoryMock.findById(eq(fileId))).thenReturn(Optional.of(entityStub));

        //when
        LessonFile file = testee.readLessonFile(fileId);

        //then
        verify(fileRepositoryMock).findById(eq(fileId));
        Assertions.assertNotNull(file);
        Assertions.assertEquals(entityStub.getId(), file.getId());
        Assertions.assertEquals(entityStub.getName(), file.getName());
        Assertions.assertEquals(entityStub.getExtension(), file.getExtension());
        Assertions.assertEquals(entityStub.getContent(), file.getContent());
        Assertions.assertEquals(entityStub.getCreatedBy(), file.getCreatedBy());
        Assertions.assertEquals(entityStub.getCreationDateTime(), file.getCreationDateTime());
    }

    @Test
    public void shouldReadFilesInformationCorrectly() {
        //given
        String fileOwnerIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        List<FileWithoutContent> filesWithoutContentStub = Lists.newArrayList(LessonFileWithoutContentStub.create());

        when(fileRepositoryMock.findAllByCreatedBy(eq(fileOwnerIdStub))).thenReturn(filesWithoutContentStub);

        //when
        List<LessonFileResponse> responses = testee.readFilesInformation(fileOwnerIdStub);

        //then
        verify(fileRepositoryMock).findAllByCreatedBy(eq(fileOwnerIdStub));

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListWhenFilesIdsListIsEmptyDuringAttachingFilesToLesson() {
        //given
        String lessonIdStub = INDIVIDUAL_LESSON_ID_STUB;
        List<String> filesIdsStub = Collections.emptyList();

        //when
        List<LessonFileAccessEntity> entities = testee.attachFilesToLesson(lessonIdStub, filesIdsStub);

        //then
        assertNotNull(entities);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void shouldReturnCorrectListWhenAttachingFilesToLesson() {
        //given
        String lessonIdStub = INDIVIDUAL_LESSON_ID_STUB;
        List<String> filesIdsStub = FILES_IDS_STUB;
        List<LessonFileAccessEntity> lessonFileAccessEntitiesStub = Collections.singletonList(LessonFileAccessEntityStub.create(lessonIdStub, filesIdsStub.get(0)));

        when(lessonFileAccessRepositoryMock.saveAll(lessonFileAccessEntitiesStub)).thenReturn(lessonFileAccessEntitiesStub);

        //when
        List<LessonFileAccessEntity> result = testee.attachFilesToLesson(lessonIdStub, filesIdsStub);

        //then
        verify(lessonFileAccessRepositoryMock).saveAll(lessonFileAccessEntitiesStub);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(lessonFileAccessEntitiesStub.size(), result.size());
        assertEquals(lessonFileAccessEntitiesStub.get(0), result.get(0));
    }
}