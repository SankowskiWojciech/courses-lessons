package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonFileEntityStub;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class LessonFileEntityToLessonFileResponseTest {

    private final LessonFileEntityToLessonFileResponse testee = LessonFileEntityToLessonFileResponse.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        FileEntity stub = LessonFileEntityStub.create();

        //when
        LessonFileResponse response = testee.apply(stub);

        //then
        assertNotNull(response);
        assertEquals(stub.getId(), response.getId());
        assertEquals(stub.getName(), response.getName());
        assertEquals(stub.getExtension(), response.getExtension());
        assertEquals(stub.getCreatedBy(), response.getCreatedBy());
        assertEquals(stub.getCreationDateTime(), response.getCreationDateTime());
    }
}