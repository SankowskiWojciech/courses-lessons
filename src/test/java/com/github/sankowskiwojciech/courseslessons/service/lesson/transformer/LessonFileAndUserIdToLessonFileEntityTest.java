package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileStub;
import org.junit.Test;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LessonFileAndUserIdToLessonFileEntityTest {

    private final LessonFileAndUserIdToLessonFileEntity testee = LessonFileAndUserIdToLessonFileEntity.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        LessonFile lessonFileStub = LessonFileStub.create();
        String userId = TUTOR_EMAIL_ADDRESS_STUB;

        //when
        FileEntity fileEntity = testee.apply(lessonFileStub, userId);

        //then
        assertNotNull(fileEntity);
        assertEquals(lessonFileStub.getName(), fileEntity.getName());
        assertEquals(lessonFileStub.getExtension(), fileEntity.getExtension());
        assertEquals(lessonFileStub.getContent(), fileEntity.getContent());
        assertEquals(userId, fileEntity.getCreatedBy());
        assertNotNull(fileEntity.getCreationDateTime());
    }
}