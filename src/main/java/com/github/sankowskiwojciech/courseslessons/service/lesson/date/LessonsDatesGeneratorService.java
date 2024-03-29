package com.github.sankowskiwojciech.courseslessons.service.lesson.date;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.DayOfWeekWithTimes;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;

import java.time.LocalDate;
import java.util.List;

public interface LessonsDatesGeneratorService {
    List<LessonDates> generateLessonsDatesWithFixedBeginningDateAndEndDate(LocalDate beginningDate, LocalDate endDate, List<DayOfWeekWithTimes> daysOfWeekWithTimes);

    List<LessonDates> generateLessonsDatesForFixedDurationLessons(LocalDate beginningDate, long durationOfAllLessonsInMinutes, List<DayOfWeekWithTimes> daysOfWeekWithTimes);
}
