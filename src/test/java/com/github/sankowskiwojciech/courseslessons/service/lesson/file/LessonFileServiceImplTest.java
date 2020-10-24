package com.github.sankowskiwojciech.courseslessons.service.lesson.file;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileStub;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.Optional;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LessonFileServiceImplTest {

    private final LessonFileRepository lessonFileRepositoryMock = mock(LessonFileRepository.class);
    private final LessonFileService testee = new LessonFileServiceImpl(lessonFileRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(lessonFileRepositoryMock);
    }

    @Test
    public void shouldCreateLessonFileCorrectly() {
        //given
        LessonFile lessonFileStub = LessonFileStub.create();
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        LessonFileEntity lessonFileEntityStub = LessonFileEntityStub.create();

        when(lessonFileRepositoryMock.save(any(LessonFileEntity.class))).thenReturn(lessonFileEntityStub);

        //when
        LessonFileResponse lessonFileResponse = testee.createLessonFile(lessonFileStub, userIdStub);

        //then
        verify(lessonFileRepositoryMock).save(any(LessonFileEntity.class));

        assertNotNull(lessonFileResponse);
        assertEquals(lessonFileEntityStub.getFileId(), lessonFileResponse.getFileId());
        assertEquals(lessonFileEntityStub.getName(), lessonFileResponse.getName());
        assertEquals(lessonFileEntityStub.getExtension(), lessonFileResponse.getExtension());
        assertEquals(lessonFileEntityStub.getCreatedBy(), lessonFileResponse.getCreatedBy());
        assertEquals(lessonFileEntityStub.getCreationDateTime(), lessonFileResponse.getCreationDateTime());
    }

    @Test
    public void shouldReadFileCorrectly() {
        //given
        long fileId = FILE_ID_STUB;
        LessonFileEntity lessonFileEntityStub = LessonFileEntityStub.create();

        when(lessonFileRepositoryMock.findById(eq(fileId))).thenReturn(Optional.of(lessonFileEntityStub));

        //when
        LessonFile lessonFile = testee.readLessonFile(fileId);

        //then
        verify(lessonFileRepositoryMock).findById(eq(fileId));
        Assertions.assertNotNull(lessonFile);
        Assertions.assertEquals(lessonFileEntityStub.getFileId(), lessonFile.getFileId());
        Assertions.assertEquals(lessonFileEntityStub.getName(), lessonFile.getName());
        Assertions.assertEquals(lessonFileEntityStub.getExtension(), lessonFile.getExtension());
        Assertions.assertEquals(lessonFileEntityStub.getContent(), lessonFile.getContent());
        Assertions.assertEquals(lessonFileEntityStub.getCreatedBy(), lessonFile.getCreatedBy());
        Assertions.assertEquals(lessonFileEntityStub.getCreationDateTime(), lessonFile.getCreationDateTime());
    }
}