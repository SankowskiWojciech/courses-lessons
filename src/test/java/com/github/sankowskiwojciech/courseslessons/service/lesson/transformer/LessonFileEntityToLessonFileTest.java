package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileEntity;
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
        FileEntity fileEntityStub = LessonFileEntityStub.create();

        //when
        LessonFile lessonFile = testee.apply(fileEntityStub);

        //then
        assertNotNull(lessonFile);
        assertEquals(fileEntityStub.getId(), lessonFile.getId());
        assertEquals(fileEntityStub.getName(), lessonFile.getName());
        assertEquals(fileEntityStub.getExtension(), lessonFile.getExtension());
        assertEquals(fileEntityStub.getContent(), lessonFile.getContent());
        assertEquals(fileEntityStub.getCreatedBy(), lessonFile.getCreatedBy());
        assertEquals(fileEntityStub.getCreationDateTime(), lessonFile.getCreationDateTime());
    }
}