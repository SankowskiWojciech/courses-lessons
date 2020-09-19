package com.github.sankowskiwojciech.courseslessons.service.lesson;

import com.github.sankowskiwojciech.courseslessons.model.lesson.DayOfWeekWithTimes;
import com.github.sankowskiwojciech.courseslessons.model.lesson.LessonDates;

import java.time.LocalDate;
import java.util.List;

public interface DatesGeneratorService {
    List<LessonDates> generateLessonsDatesWithFixedBeginningDateAndEndDate(LocalDate beginningDate, LocalDate endDate, List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes);
}
