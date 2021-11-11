package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonFileStub;
import org.junit.Test;

import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LessonFileAndUserIdToLessonFileEntityTest {

    private final LessonFileAndUserIdToLessonFileEntity testee = LessonFileAndUserIdToLessonFileEntity.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        LessonFile stub = LessonFileStub.create();
        String userId = TUTOR_EMAIL_ADDRESS_STUB;

        //when
        FileEntity entity = testee.apply(stub, userId);

        //then
        assertNotNull(entity);
        assertEquals(stub.getName(), entity.getName());
        assertEquals(stub.getExtension(), entity.getExtension());
        assertEquals(stub.getContent(), entity.getContent());
        assertEquals(userId, entity.getCreatedBy());
        assertNotNull(entity.getCreationDateTime());
    }
}