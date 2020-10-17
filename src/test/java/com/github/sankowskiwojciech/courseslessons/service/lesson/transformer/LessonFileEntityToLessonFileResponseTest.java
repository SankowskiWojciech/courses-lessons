package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileEntity;
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
        LessonFileEntity lessonFileEntityStub = LessonFileEntityStub.create();

        //when
        LessonFileResponse lessonFileResponse = testee.apply(lessonFileEntityStub);

        //then
        assertNotNull(lessonFileResponse);
        assertEquals(lessonFileEntityStub.getFileId(), lessonFileResponse.getFileId());
        assertEquals(lessonFileEntityStub.getName(), lessonFileResponse.getName());
        assertEquals(lessonFileEntityStub.getExtension(), lessonFileResponse.getExtension());
        assertEquals(lessonFileEntityStub.getCreatedBy(), lessonFileResponse.getCreatedBy());
        assertEquals(lessonFileEntityStub.getCreationDateTime(), lessonFileResponse.getCreationDateTime());
    }
}