package com.github.sankowskiwojciech.courseslessons.service.lesson.date;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.DayOfWeekWithTimes;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;
import com.github.sankowskiwojciech.courseslessons.stub.DayOfWeekWithTimesStub;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LessonsDatesGeneratorServiceImplTest {

    private static final String BEGINNING_DATE_STUB = "2020-09-21";
    private static final String END_DATE_STUB = "2020-10-04";
    private static final String START_TIME_STUB = "14:00";
    private static final String END_TIME_STUB = "16:00";
    private static final int GENERATED_DATES_FOR_FIXED_BEGINNING_AND_END_DATES_EXPECTED_SIZE = 6;
    private static final int GENERATED_DATES_FOR_FIXED_DURATION_LESSONS_EXPECTED_SIZE = 3;
    private static final long DURATION_OF_ALL_LESSONS_IN_MINUTES_STUB = 360L;

    private final LessonsDatesGeneratorService testee = new LessonsDatesGeneratorServiceImpl();

    @Test
    public void shouldGenerateLessonsDatesWithFixedBeginningDateAndEndDateCorrectly() {
        //given
        LocalDate beginningDateStub = LocalDate.parse(BEGINNING_DATE_STUB);
        LocalDate endDateStub = LocalDate.parse(END_DATE_STUB);
        LocalTime startTimeStub = LocalTime.parse(START_TIME_STUB);
        LocalTime endTimeStub = LocalTime.parse(END_TIME_STUB);
        List<DayOfWeekWithTimes> daysOfWeekWithTimes = Lists.newArrayList(
                DayOfWeekWithTimesStub.create(DayOfWeek.MONDAY, startTimeStub, endTimeStub),
                DayOfWeekWithTimesStub.create(DayOfWeek.WEDNESDAY, startTimeStub, endTimeStub),
                DayOfWeekWithTimesStub.create(DayOfWeek.FRIDAY, startTimeStub, endTimeStub)
        );

        //when
        List<LessonDates> dates = testee.generateLessonsDatesWithFixedBeginningDateAndEndDate(beginningDateStub, endDateStub, daysOfWeekWithTimes);

        //then
        assertNotNull(dates);
        assertEquals(GENERATED_DATES_FOR_FIXED_BEGINNING_AND_END_DATES_EXPECTED_SIZE, dates.size());
    }

    @Test
    public void shouldGenerateLessonsDatesForFixedDurationLessonsCorrectly() {
        //given
        LocalDate beginningDateStub = LocalDate.parse(BEGINNING_DATE_STUB);
        LocalTime startTimeStub = LocalTime.parse(START_TIME_STUB);
        LocalTime endTimeStub = LocalTime.parse(END_TIME_STUB);
        List<DayOfWeekWithTimes> daysOfWeekWithTimes = Lists.newArrayList(
                DayOfWeekWithTimesStub.create(DayOfWeek.MONDAY, startTimeStub, endTimeStub),
                DayOfWeekWithTimesStub.create(DayOfWeek.WEDNESDAY, startTimeStub, endTimeStub),
                DayOfWeekWithTimesStub.create(DayOfWeek.FRIDAY, startTimeStub, endTimeStub)
        );
        long durationOfAllLessonsInMinutes = DURATION_OF_ALL_LESSONS_IN_MINUTES_STUB;

        //when
        List<LessonDates> dates = testee.generateLessonsDatesForFixedDurationLessons(beginningDateStub, durationOfAllLessonsInMinutes, daysOfWeekWithTimes);

        //then
        assertNotNull(dates);
        assertEquals(GENERATED_DATES_FOR_FIXED_DURATION_LESSONS_EXPECTED_SIZE, dates.size());
    }
}