package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileWithoutContentStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonFileWithoutContentToLessonFileResponseTest {

    private static final LessonFileWithoutContentToLessonFileResponse testee = LessonFileWithoutContentToLessonFileResponse.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        FileWithoutContent fileStub = LessonFileWithoutContentStub.create();

        //when
        LessonFileResponse response = testee.apply(fileStub);

        //then
        assertNotNull(response);
        assertEquals(fileStub.getId(), response.getId());
        assertEquals(fileStub.getName(), response.getName());
        assertEquals(fileStub.getExtension(), response.getExtension());
        assertEquals(fileStub.getCreatedBy(), response.getCreatedBy());
        assertEquals(fileStub.getCreationDateTime(), response.getCreationDateTime());
    }
}