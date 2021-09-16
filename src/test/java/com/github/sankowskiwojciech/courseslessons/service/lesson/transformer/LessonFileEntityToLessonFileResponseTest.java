package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileEntityStub;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class LessonFileEntityToLessonFileResponseTest {

    private final LessonFileEntityToLessonFileResponse testee = LessonFileEntityToLessonFileResponse.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        FileEntity fileEntityStub = LessonFileEntityStub.create();

        //when
        LessonFileResponse lessonFileResponse = testee.apply(fileEntityStub);

        //then
        assertNotNull(lessonFileResponse);
        assertEquals(fileEntityStub.getId(), lessonFileResponse.getId());
        assertEquals(fileEntityStub.getName(), lessonFileResponse.getName());
        assertEquals(fileEntityStub.getExtension(), lessonFileResponse.getExtension());
        assertEquals(fileEntityStub.getCreatedBy(), lessonFileResponse.getCreatedBy());
        assertEquals(fileEntityStub.getCreationDateTime(), lessonFileResponse.getCreationDateTime());
    }
}