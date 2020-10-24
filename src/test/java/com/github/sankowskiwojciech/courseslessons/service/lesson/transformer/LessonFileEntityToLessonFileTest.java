package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonFileEntityToLessonFileTest {

    private final LessonFileEntityToLessonFile testee = LessonFileEntityToLessonFile.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        LessonFileEntity lessonFileEntityStub = LessonFileEntityStub.create();

        //when
        LessonFile lessonFile = testee.apply(lessonFileEntityStub);

        //then
        assertNotNull(lessonFile);
        assertEquals(lessonFileEntityStub.getFileId(), lessonFile.getFileId());
        assertEquals(lessonFileEntityStub.getName(), lessonFile.getName());
        assertEquals(lessonFileEntityStub.getExtension(), lessonFile.getExtension());
        assertEquals(lessonFileEntityStub.getContent(), lessonFile.getContent());
        assertEquals(lessonFileEntityStub.getCreatedBy(), lessonFile.getCreatedBy());
        assertEquals(lessonFileEntityStub.getCreationDateTime(), lessonFile.getCreationDateTime());
    }
}