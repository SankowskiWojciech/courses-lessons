package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonFileEntityStub {

    public static IndividualLessonFileEntity create(long lessonId, long fileId) {
        return new IndividualLessonFileEntity(lessonId, fileId);
    }
}
