package com.github.sankowskiwojciech.courseslessons.service.lesson.date;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.DayOfWeekWithTimes;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LessonsDatesGeneratorServiceImpl implements LessonsDatesGeneratorService {

    @Override
    public List<LessonDates> generateLessonsDatesWithFixedBeginningDateAndEndDate(LocalDate beginningDate, LocalDate endDate, List<DayOfWeekWithTimes> daysOfWeekWithTimes) {
        Map<DayOfWeek, DayOfWeekWithTimes> daysOfWeekWithTimesMap = transformLessonsDaysOfWeekWithTimesToMap(daysOfWeekWithTimes);
        LocalDate currentDate = LocalDate.from(beginningDate);
        List<LessonDates> generatedDates = new ArrayList<>();
        while (!currentDate.isAfter(endDate)) {
            addCurrentDateToGeneratedLessonsDatesIfDayOfWeekMatches(daysOfWeekWithTimesMap, currentDate, generatedDates);
            currentDate = currentDate.plusDays(1);
        }
        return generatedDates;
    }

    @Override
    public List<LessonDates> generateLessonsDatesForFixedDurationLessons(LocalDate beginningDate, long durationOfAllLessonsInMinutes, List<DayOfWeekWithTimes> daysOfWeekWithTimes) {
        Map<DayOfWeek, DayOfWeekWithTimes> daysOfWeekWithTimesMap = transformLessonsDaysOfWeekWithTimesToMap(daysOfWeekWithTimes);
        LocalDate currentDate = LocalDate.from(beginningDate);
        List<LessonDates> generatedDates = new ArrayList<>();
        while (durationOfAllLessonsInMinutes > 0) {
            if (daysOfWeekWithTimesMap.containsKey(currentDate.getDayOfWeek())) {
                DayOfWeekWithTimes dayOfWeekWithTimes = daysOfWeekWithTimesMap.get(currentDate.getDayOfWeek());
                generatedDates.add(new LessonDates(currentDate.atTime(dayOfWeekWithTimes.getStartTime()), currentDate.atTime(dayOfWeekWithTimes.getEndTime())));
                durationOfAllLessonsInMinutes -= Duration.between(dayOfWeekWithTimes.getStartTime(), dayOfWeekWithTimes.getEndTime()).toMinutes();
            }
            currentDate = currentDate.plusDays(1);
        }
        return generatedDates;
    }

    private void addCurrentDateToGeneratedLessonsDatesIfDayOfWeekMatches(Map<DayOfWeek, DayOfWeekWithTimes> daysOfWeekWithTimesMap, LocalDate currentDate, List<LessonDates> generatedDates) {
        if (daysOfWeekWithTimesMap.containsKey(currentDate.getDayOfWeek())) {
            DayOfWeekWithTimes dayOfWeekWithTimes = daysOfWeekWithTimesMap.get(currentDate.getDayOfWeek());
            generatedDates.add(new LessonDates(currentDate.atTime(dayOfWeekWithTimes.getStartTime()), currentDate.atTime(dayOfWeekWithTimes.getEndTime())));
        }
    }

    private Map<DayOfWeek, DayOfWeekWithTimes> transformLessonsDaysOfWeekWithTimesToMap(List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes) {
        return lessonsDaysOfWeekWithTimes.stream().collect(Collectors.toMap(DayOfWeekWithTimes::getDayOfWeek, dayOfWeekWithTimes -> dayOfWeekWithTimes));
    }
}
