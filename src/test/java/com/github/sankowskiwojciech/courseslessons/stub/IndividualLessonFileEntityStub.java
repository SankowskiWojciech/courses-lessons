package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonFileEntityStub {

    public static LessonFileAccessEntity create(String lessonId, String fileId) {
        return new LessonFileAccessEntity(lessonId, fileId);
    }
}
