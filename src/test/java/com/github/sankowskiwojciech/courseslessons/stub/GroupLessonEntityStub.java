package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_DESCRIPTION_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_TITLE_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupLessonEntityStub {
    public static GroupLessonEntity createWithDatesOfLesson(LocalDateTime startDate, LocalDateTime endDate) {
        final GroupLessonEntity entity = create();
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        return entity;
    }

    public static GroupLessonEntity create() {
        final LocalDateTime currentDateTime = LocalDateTime.now().plusHours(1);
        return new GroupLessonEntity(null, LESSON_TITLE_STUB, currentDateTime, currentDateTime.plusHours(3), LESSON_DESCRIPTION_STUB, currentDateTime, currentDateTime.plusMinutes(30), SubdomainEntityStub.create(), TutorEntityStub.create(), GroupEntityStub.create());
    }
}
