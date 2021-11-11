package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonFileEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonFileEntityToLessonFileTest {

    private final LessonFileEntityToLessonFile testee = LessonFileEntityToLessonFile.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        FileEntity stub = LessonFileEntityStub.create();

        //when
        LessonFile file = testee.apply(stub);

        //then
        assertNotNull(file);
        assertEquals(stub.getId(), file.getId());
        assertEquals(stub.getName(), file.getName());
        assertEquals(stub.getExtension(), file.getExtension());
        assertEquals(stub.getContent(), file.getContent());
        assertEquals(stub.getCreatedBy(), file.getCreatedBy());
        assertEquals(stub.getCreationDateTime(), file.getCreationDateTime());
    }
}