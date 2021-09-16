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
        return DayOfWeekWithTimes.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(currentTime)
                .endTime(currentTime.minusHours(2))
                .build();
    }

    public static DayOfWeekWithTimes createValid() {
        LocalTime startTime = LocalTime.of(10, 0);
        return DayOfWeekWithTimes.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(startTime)
                .endTime(startTime.plusHours(2))
                .build();
    }

    public static DayOfWeekWithTimes create(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        return DayOfWeekWithTimes.builder()
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
