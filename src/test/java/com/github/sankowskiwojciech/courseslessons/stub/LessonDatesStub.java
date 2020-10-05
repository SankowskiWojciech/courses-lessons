package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonDatesStub {

    public static LessonDates createWithDates(LocalDateTime startDate, LocalDateTime endDate) {
        return new LessonDates(startDate, endDate);
    }
}
