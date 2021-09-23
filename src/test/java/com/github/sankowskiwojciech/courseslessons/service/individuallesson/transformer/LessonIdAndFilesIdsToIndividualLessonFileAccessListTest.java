package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import org.junit.Test;

import java.util.List;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILES_IDS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.INDIVIDUAL_LESSON_ID_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class LessonIdAndFilesIdsToIndividualLessonFileAccessListTest {

    private final LessonIdAndFilesIdsToIndividualLessonFileAccessList testee = LessonIdAndFilesIdsToIndividualLessonFileAccessList.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        String lessonIdStub = INDIVIDUAL_LESSON_ID_STUB;
        List<String> filesIdsStub = FILES_IDS_STUB;

        //when
        List<LessonFileAccessEntity> lessonFileAccessEntityList = testee.apply(lessonIdStub, filesIdsStub);

        //then
        assertNotNull(lessonFileAccessEntityList);
        assertFalse(lessonFileAccessEntityList.isEmpty());
        assertEquals(filesIdsStub.size(), lessonFileAccessEntityList.size());
    }
}