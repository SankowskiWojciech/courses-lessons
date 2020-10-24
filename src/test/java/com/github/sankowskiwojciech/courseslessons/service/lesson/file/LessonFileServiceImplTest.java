package com.github.sankowskiwojciech.courseslessons.service.lesson.file;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonFileEntityStub;
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
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.INDIVIDUAL_LESSON_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LessonFileServiceImplTest {

    private final LessonFileRepository lessonFileRepositoryMock = mock(LessonFileRepository.class);
    private final IndividualLessonRepository individualLessonRepositoryMock = mock(IndividualLessonRepository.class);
    private final IndividualLessonFileRepository individualLessonFileRepositoryMock = mock(IndividualLessonFileRepository.class);
    private final LessonFileService testee = new LessonFileServiceImpl(lessonFileRepositoryMock, individualLessonRepositoryMock, individualLessonFileRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(lessonFileRepositoryMock, individualLessonRepositoryMock, individualLessonFileRepositoryMock);
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

    @Test
    public void shouldReadFilesInformationCorrectly() {
        //given
        String userId = TUTOR_EMAIL_ADDRESS_STUB;
        List<IndividualLessonEntity> lessonEntitiesRelatedToUserStub = Lists.newArrayList(IndividualLessonEntityStub.create());
        List<IndividualLessonFileEntity> lessonsFilesRelatedToUserStub = Lists.newArrayList(IndividualLessonFileEntityStub.create(INDIVIDUAL_LESSON_ID_STUB, FILE_ID_STUB));
        List<LessonFileWithoutContent> lessonFilesWithoutContentStub = Lists.newArrayList(LessonFileWithoutContentStub.create());

        when(individualLessonRepositoryMock.findAllByUserId(eq(userId))).thenReturn(lessonEntitiesRelatedToUserStub);
        when(individualLessonFileRepositoryMock.findAllByLessonIdIn(anyList())).thenReturn(lessonsFilesRelatedToUserStub);
        when(lessonFileRepositoryMock.findAllByFileIdIn(anySet())).thenReturn(lessonFilesWithoutContentStub);

        //when
        List<LessonFileResponse> lessonFileResponses = testee.readFilesInformation(userId);

        //then
        verify(individualLessonRepositoryMock).findAllByUserId(eq(userId));
        verify(individualLessonFileRepositoryMock).findAllByLessonIdIn(anyList());
        verify(lessonFileRepositoryMock).findAllByFileIdIn(anySet());

        assertNotNull(lessonFileResponses);
        assertFalse(lessonFileResponses.isEmpty());
    }
}