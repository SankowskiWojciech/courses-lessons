package com.github.sankowskiwojciech.courseslessons.service.lesson.file;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileWithoutContentStub;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LessonFileServiceImplTest {

    private final FileRepository fileRepositoryMock = mock(FileRepository.class);
    private final LessonFileService testee = new LessonFileServiceImpl(fileRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(fileRepositoryMock);
    }

    @Test
    public void shouldCreateLessonFileCorrectly() {
        //given
        LessonFile lessonFileStub = LessonFileStub.create();
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        FileEntity fileEntityStub = LessonFileEntityStub.create();

        when(fileRepositoryMock.save(any(FileEntity.class))).thenReturn(fileEntityStub);

        //when
        LessonFileResponse lessonFileResponse = testee.createLessonFile(lessonFileStub, userIdStub);

        //then
        verify(fileRepositoryMock).save(any(FileEntity.class));

        assertNotNull(lessonFileResponse);
        assertEquals(fileEntityStub.getId(), lessonFileResponse.getId());
        assertEquals(fileEntityStub.getName(), lessonFileResponse.getName());
        assertEquals(fileEntityStub.getExtension(), lessonFileResponse.getExtension());
        assertEquals(fileEntityStub.getCreatedBy(), lessonFileResponse.getCreatedBy());
        assertEquals(fileEntityStub.getCreationDateTime(), lessonFileResponse.getCreationDateTime());
    }

    @Test
    public void shouldReadFileCorrectly() {
        //given
        String fileId = FILE_ID_STUB;
        FileEntity fileEntityStub = LessonFileEntityStub.create();

        when(fileRepositoryMock.findById(eq(fileId))).thenReturn(Optional.of(fileEntityStub));

        //when
        LessonFile lessonFile = testee.readLessonFile(fileId);

        //then
        verify(fileRepositoryMock).findById(eq(fileId));
        Assertions.assertNotNull(lessonFile);
        Assertions.assertEquals(fileEntityStub.getId(), lessonFile.getId());
        Assertions.assertEquals(fileEntityStub.getName(), lessonFile.getName());
        Assertions.assertEquals(fileEntityStub.getExtension(), lessonFile.getExtension());
        Assertions.assertEquals(fileEntityStub.getContent(), lessonFile.getContent());
        Assertions.assertEquals(fileEntityStub.getCreatedBy(), lessonFile.getCreatedBy());
        Assertions.assertEquals(fileEntityStub.getCreationDateTime(), lessonFile.getCreationDateTime());
    }

    @Test
    public void shouldReadFilesInformationCorrectly() {
        //given
        String fileOwnerIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        List<FileWithoutContent> lessonFilesWithoutContentStub = Lists.newArrayList(LessonFileWithoutContentStub.create());

        when(fileRepositoryMock.findAllByCreatedBy(eq(fileOwnerIdStub))).thenReturn(lessonFilesWithoutContentStub);

        //when
        List<LessonFileResponse> lessonFileResponses = testee.readFilesInformation(fileOwnerIdStub);

        //then
        verify(fileRepositoryMock).findAllByCreatedBy(eq(fileOwnerIdStub));

        assertNotNull(lessonFileResponses);
        assertFalse(lessonFileResponses.isEmpty());
    }
}