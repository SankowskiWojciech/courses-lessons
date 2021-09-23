package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.DayOfWeekWithTimes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DayOfWeekWithTimesStub {

    public static DayOfWeekWithTimes createInvalid() {
        LocalTime currentTime = LocalTime.now();
        return create(DayOfWeek.MONDAY, currentTime, currentTime.minusHours(2));
    }

    public static DayOfWeekWithTimes createValid() {
        LocalTime startTime = LocalTime.of(10, 0);
        return create(DayOfWeek.MONDAY, startTime, startTime.plusHours(2));
    }

    public static DayOfWeekWithTimes create(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        return DayOfWeekWithTimes.builder()
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
