package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.exception.file.FileCorruptedException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.file.FileNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.file.InvalidFileFormatException;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.courseslessons.stub.MultipartFileStub;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_ID_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LessonFileValidatorServiceImplTest {

    private final Detector detectorMock = mock(Detector.class);
    private final Set<String> validFileMIMETypesMock = mock(Set.class);
    private final LessonFileRepository lessonFileRepositoryMock = mock(LessonFileRepository.class);
    private final LessonFileValidatorService testee = new LessonFileValidatorServiceImpl(detectorMock, validFileMIMETypesMock, lessonFileRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(detectorMock, validFileMIMETypesMock, lessonFileRepositoryMock);
    }

    @Test(expected = FileCorruptedException.class)
    public void shouldThrowFileCorruptedExceptionWhenFileIsCorrupted() throws IOException {
        //given
        MultipartFile multipartFileStub = MultipartFileStub.create();
        when(detectorMock.detect(any(InputStream.class), any(Metadata.class))).thenThrow(IOException.class);

        //when
        try {
            LessonFile lessonFile = testee.validateUploadedFile(multipartFileStub);
        } catch (FileCorruptedException e) {
            //then exception is thrown
            verify(detectorMock).detect(any(InputStream.class), any(Metadata.class));
            throw e;
        }
    }

    @Test(expected = InvalidFileFormatException.class)
    public void shouldThrowInvalidFileFormatExceptionWhenFileFormatIsInvalid() throws IOException {
        //given
        MultipartFile multipartFileStub = MultipartFileStub.create();
        MediaType mediaTypeStub = MediaType.APPLICATION_XML;
        boolean isValidFileMIMEType = false;
        when(detectorMock.detect(any(InputStream.class), any(Metadata.class))).thenReturn(mediaTypeStub);
        when(validFileMIMETypesMock.contains(eq(mediaTypeStub.toString()))).thenReturn(isValidFileMIMEType);

        //when
        try {
            LessonFile lessonFile = testee.validateUploadedFile(multipartFileStub);
        } catch (InvalidFileFormatException e) {
            //then exception is thrown
            verify(detectorMock).detect(any(InputStream.class), any(Metadata.class));
            verify(validFileMIMETypesMock).contains(eq(mediaTypeStub.toString()));
            throw e;
        }
    }

    @Test
    public void shouldReturnLessonFileWhenFileIsValid() throws IOException {
        //given
        MultipartFile multipartFileStub = MultipartFileStub.create();
        MediaType mediaTypeStub = MediaType.APPLICATION_XML;
        boolean isValidFileMIMEType = true;

        when(detectorMock.detect(any(InputStream.class), any(Metadata.class))).thenReturn(mediaTypeStub);
        when(validFileMIMETypesMock.contains(eq(mediaTypeStub.toString()))).thenReturn(isValidFileMIMEType);

        //when
        LessonFile lessonFile = testee.validateUploadedFile(multipartFileStub);

        //then
        verify(detectorMock).detect(any(InputStream.class), any(Metadata.class));
        verify(validFileMIMETypesMock).contains(eq(mediaTypeStub.toString()));

        assertNotNull(lessonFile);
        assertEquals(0, lessonFile.getFileId());
        assertNull(lessonFile.getCreatedBy());
        assertNull(lessonFile.getCreationDateTime());
        assertEquals(multipartFileStub.getOriginalFilename(), lessonFile.getName());
        assertEquals(FilenameUtils.getExtension(multipartFileStub.getOriginalFilename()), lessonFile.getExtension());
        assertEquals(multipartFileStub.getBytes(), lessonFile.getContent());
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowFileNotFoundExceptionWhenFileDoesNotExist() {
        //given
        Long fileId = FILE_ID_STUB;
        boolean doesFileExist = false;

        when(lessonFileRepositoryMock.existsById(eq(fileId))).thenReturn(doesFileExist);

        //when
        try {
            testee.validateIfFileExists(fileId);
        } catch (FileNotFoundException e) {

            //then exception is thrown
            verify(lessonFileRepositoryMock).existsById(eq(fileId));
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenFileExists() {
        //given
        Long fileId = FILE_ID_STUB;
        boolean doesFileExist = true;

        when(lessonFileRepositoryMock.existsById(eq(fileId))).thenReturn(doesFileExist);

        //when
        testee.validateIfFileExists(fileId);

        //then nothing happens
        verify(lessonFileRepositoryMock).existsById(eq(fileId));
    }
}
