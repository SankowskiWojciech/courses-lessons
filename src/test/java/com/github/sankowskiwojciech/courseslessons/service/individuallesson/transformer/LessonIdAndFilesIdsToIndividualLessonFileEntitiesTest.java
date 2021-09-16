package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import org.junit.Test;

import java.util.List;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILES_IDS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.INDIVIDUAL_LESSON_ID_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class LessonIdAndFilesIdsToIndividualLessonFileEntitiesTest {

    private final LessonIdAndFilesIdsToIndividualLessonFileEntities testee = LessonIdAndFilesIdsToIndividualLessonFileEntities.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        String lessonIdStub = INDIVIDUAL_LESSON_ID_STUB;
        List<String> filesIdsStub = FILES_IDS_STUB;

        //when
        List<LessonFileAccessEntity> individualLessonFileEntities = testee.apply(lessonIdStub, filesIdsStub);

        //then
        assertNotNull(individualLessonFileEntities);
        assertFalse(individualLessonFileEntities.isEmpty());
        assertEquals(filesIdsStub.size(), individualLessonFileEntities.size());
    }
}