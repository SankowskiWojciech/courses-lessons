package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.courseslessons.stub.MultipartFileStub;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.Assert.*;

public class MultipartFileToLessonFileTest {

    private final MultipartFileToLessonFile testee = MultipartFileToLessonFile.getInstance();

    @Test
    public void shouldReturnLessonFileWhenFileIsCorrect() throws IOException {
        //given
        MultipartFile multipartFileStub = MultipartFileStub.create();

        //when
        LessonFile lessonFile = testee.apply(multipartFileStub);

        //then
        assertNotNull(lessonFile);
        assertNull(lessonFile.getFileId());
        assertNull(lessonFile.getCreatedBy());
        assertNull(lessonFile.getCreationDateTime());
        assertEquals(multipartFileStub.getOriginalFilename(), lessonFile.getName());
        assertEquals(FilenameUtils.getExtension(multipartFileStub.getOriginalFilename()), lessonFile.getExtension());
        assertEquals(multipartFileStub.getBytes(), lessonFile.getContent());
    }
}