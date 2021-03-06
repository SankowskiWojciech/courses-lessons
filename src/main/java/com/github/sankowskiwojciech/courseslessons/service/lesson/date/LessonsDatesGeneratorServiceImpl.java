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
    public List<LessonDates> generateLessonsDatesWithFixedBeginningDateAndEndDate(LocalDate beginningDate, LocalDate endDate, List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes) {
        Map<DayOfWeek, DayOfWeekWithTimes> lessonsDaysOfWeekWithTimesMap = transformLessonsDaysOfWeekWithTimesToMap(lessonsDaysOfWeekWithTimes);
        LocalDate currentDate = LocalDate.from(beginningDate);
        List<LessonDates> generatedLessonsDates = new ArrayList<>();
        while (!currentDate.isAfter(endDate)) {
            addCurrentDateToGeneratedLessonsDatesIfDayOfWeekMatches(lessonsDaysOfWeekWithTimesMap, currentDate, generatedLessonsDates);
            currentDate = currentDate.plusDays(1);
        }
        return generatedLessonsDates;
    }

    @Override
    public List<LessonDates> generateLessonsDatesForFixedDurationLessons(LocalDate beginningDate, long allLessonsDurationInMinutes, List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes) {
        Map<DayOfWeek, DayOfWeekWithTimes> lessonsDaysOfWeekWithTimesMap = transformLessonsDaysOfWeekWithTimesToMap(lessonsDaysOfWeekWithTimes);
        LocalDate currentDate = LocalDate.from(beginningDate);
        List<LessonDates> generatedLessonsDates = new ArrayList<>();
        while (allLessonsDurationInMinutes > 0) {
            if (lessonsDaysOfWeekWithTimesMap.containsKey(currentDate.getDayOfWeek())) {
                DayOfWeekWithTimes dayOfWeekWithTimes = lessonsDaysOfWeekWithTimesMap.get(currentDate.getDayOfWeek());
                generatedLessonsDates.add(new LessonDates(currentDate.atTime(dayOfWeekWithTimes.getStartTime()), currentDate.atTime(dayOfWeekWithTimes.getEndTime())));
                allLessonsDurationInMinutes -= Duration.between(dayOfWeekWithTimes.getStartTime(), dayOfWeekWithTimes.getEndTime()).toMinutes();
            }
            currentDate = currentDate.plusDays(1);
        }
        return generatedLessonsDates;
    }

    private void addCurrentDateToGeneratedLessonsDatesIfDayOfWeekMatches(Map<DayOfWeek, DayOfWeekWithTimes> lessonsDaysOfWeekWithTimesMap, LocalDate currentDate, List<LessonDates> generatedLessonsDates) {
        if (lessonsDaysOfWeekWithTimesMap.containsKey(currentDate.getDayOfWeek())) {
            DayOfWeekWithTimes dayOfWeekWithTimes = lessonsDaysOfWeekWithTimesMap.get(currentDate.getDayOfWeek());
            generatedLessonsDates.add(new LessonDates(currentDate.atTime(dayOfWeekWithTimes.getStartTime()), currentDate.atTime(dayOfWeekWithTimes.getEndTime())));
        }
    }

    private Map<DayOfWeek, DayOfWeekWithTimes> transformLessonsDaysOfWeekWithTimesToMap(List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes) {
        return lessonsDaysOfWeekWithTimes.stream().collect(Collectors.toMap(DayOfWeekWithTimes::getDayOfWeek, dayOfWeekWithTimes -> dayOfWeekWithTimes));
    }
}
