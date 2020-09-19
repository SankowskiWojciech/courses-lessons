package com.github.sankowskiwojciech.courseslessons.model.lesson;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum ScheduleType {
    ONE_YEAR_LENGTH_LESSONS,
    FIXED_DURATION_LESSONS,
    FIXED_DATES_LESSONS
}
