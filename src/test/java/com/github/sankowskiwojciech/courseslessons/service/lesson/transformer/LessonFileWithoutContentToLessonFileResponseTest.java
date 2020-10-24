package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileWithoutContent;
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
        LessonFileWithoutContent lessonFileWithoutContentStub = LessonFileWithoutContentStub.create();

        //when
        LessonFileResponse lessonFileResponse = testee.apply(lessonFileWithoutContentStub);

        //then
        assertNotNull(lessonFileResponse);
        assertEquals(lessonFileWithoutContentStub.getFileId(), lessonFileResponse.getFileId());
        assertEquals(lessonFileWithoutContentStub.getName(), lessonFileResponse.getName());
        assertEquals(lessonFileWithoutContentStub.getExtension(), lessonFileResponse.getExtension());
        assertEquals(lessonFileWithoutContentStub.getCreatedBy(), lessonFileResponse.getCreatedBy());
        assertEquals(lessonFileWithoutContentStub.getCreationDateTime(), lessonFileResponse.getCreationDateTime());
    }
}