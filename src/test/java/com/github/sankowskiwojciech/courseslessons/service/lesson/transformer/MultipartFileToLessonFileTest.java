package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.courseslessons.stub.MultipartFileStub;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MultipartFileToLessonFileTest {

    private final MultipartFileToLessonFile testee = MultipartFileToLessonFile.getInstance();

    @Test
    public void shouldReturnLessonFileWhenFileIsCorrect() throws IOException {
        //given
        MultipartFile stub = MultipartFileStub.create();

        //when
        LessonFile file = testee.apply(stub);

        //then
        assertNotNull(file);
        assertNull(file.getId());
        assertNull(file.getCreatedBy());
        assertNull(file.getCreationDateTime());
        assertEquals(stub.getOriginalFilename(), file.getName());
        assertEquals(FilenameUtils.getExtension(stub.getOriginalFilename()), file.getExtension());
        assertEquals(stub.getBytes(), file.getContent());
    }
}