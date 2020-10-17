package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.file.FileCorruptedException;
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
    private final LessonFileValidatorService testee = new LessonFileValidatorServiceImpl(detectorMock, validFileMIMETypesMock);

    @Before
    public void reset() {
        Mockito.reset(detectorMock, validFileMIMETypesMock);
    }

    @Test(expected = FileCorruptedException.class)
    public void shouldThrowFileCorruptedExceptionWhenFileIsCorrupted() throws IOException {
        //given
        MultipartFile multipartFileStub = MultipartFileStub.create();
        when(detectorMock.detect(any(InputStream.class), any(Metadata.class))).thenThrow(IOException.class);

        //when
        try {
            LessonFile lessonFile = testee.validateFile(multipartFileStub);
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
            LessonFile lessonFile = testee.validateFile(multipartFileStub);
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
        LessonFile lessonFile = testee.validateFile(multipartFileStub);

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
}
