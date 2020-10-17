package com.github.sankowskiwojciech.courseslessons.service.lesson.file;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
}