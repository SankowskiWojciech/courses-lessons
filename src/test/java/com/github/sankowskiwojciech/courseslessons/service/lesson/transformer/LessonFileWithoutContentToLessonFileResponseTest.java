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
        FileWithoutContent fileWithoutContentStub = LessonFileWithoutContentStub.create();

        //when
        LessonFileResponse lessonFileResponse = testee.apply(fileWithoutContentStub);

        //then
        assertNotNull(lessonFileResponse);
        assertEquals(fileWithoutContentStub.getId(), lessonFileResponse.getId());
        assertEquals(fileWithoutContentStub.getName(), lessonFileResponse.getName());
        assertEquals(fileWithoutContentStub.getExtension(), lessonFileResponse.getExtension());
        assertEquals(fileWithoutContentStub.getCreatedBy(), lessonFileResponse.getCreatedBy());
        assertEquals(fileWithoutContentStub.getCreationDateTime(), lessonFileResponse.getCreationDateTime());
    }
}