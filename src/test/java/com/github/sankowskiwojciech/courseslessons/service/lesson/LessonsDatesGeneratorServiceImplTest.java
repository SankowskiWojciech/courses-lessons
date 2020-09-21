package com.github.sankowskiwojciech.courseslessons.service.lesson;

import com.github.sankowskiwojciech.courseslessons.model.lesson.DayOfWeekWithTimes;
import com.github.sankowskiwojciech.courseslessons.model.lesson.LessonDates;
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
    private static final long ALL_LESSONS_DURATION_IN_MINUTES_STUB = 360L;

    private final LessonsDatesGeneratorService testee = new LessonsDatesGeneratorServiceImpl();

    @Test
    public void shouldGenerateLessonsDatesWithFixedBeginningDateAndEndDateCorrectly() {
        //given
        LocalDate beginningDateStub = LocalDate.parse(BEGINNING_DATE_STUB);
        LocalDate endDateStub = LocalDate.parse(END_DATE_STUB);
        LocalTime startTimeStub = LocalTime.parse(START_TIME_STUB);
        LocalTime endTimeStub = LocalTime.parse(END_TIME_STUB);
        List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes = Lists.newArrayList(
                DayOfWeekWithTimesStub.create(DayOfWeek.MONDAY, startTimeStub, endTimeStub),
                DayOfWeekWithTimesStub.create(DayOfWeek.WEDNESDAY, startTimeStub, endTimeStub),
                DayOfWeekWithTimesStub.create(DayOfWeek.FRIDAY, startTimeStub, endTimeStub)
        );

        //when
        List<LessonDates> lessonDates = testee.generateLessonsDatesWithFixedBeginningDateAndEndDate(beginningDateStub, endDateStub, lessonsDaysOfWeekWithTimes);

        //then
        assertNotNull(lessonDates);
        assertEquals(GENERATED_DATES_FOR_FIXED_BEGINNING_AND_END_DATES_EXPECTED_SIZE, lessonDates.size());
    }

    @Test
    public void shouldGenerateLessonsDatesForFixedDurationLessonsCorrectly() {
        //given
        LocalDate beginningDateStub = LocalDate.parse(BEGINNING_DATE_STUB);
        LocalTime startTimeStub = LocalTime.parse(START_TIME_STUB);
        LocalTime endTimeStub = LocalTime.parse(END_TIME_STUB);
        List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes = Lists.newArrayList(
                DayOfWeekWithTimesStub.create(DayOfWeek.MONDAY, startTimeStub, endTimeStub),
                DayOfWeekWithTimesStub.create(DayOfWeek.WEDNESDAY, startTimeStub, endTimeStub),
                DayOfWeekWithTimesStub.create(DayOfWeek.FRIDAY, startTimeStub, endTimeStub)
        );
        long allLessonsDurationInMinutes = ALL_LESSONS_DURATION_IN_MINUTES_STUB;

        //when
        List<LessonDates> lessonDates = testee.generateLessonsDatesForFixedDurationLessons(beginningDateStub, allLessonsDurationInMinutes, lessonsDaysOfWeekWithTimes);

        //then
        assertNotNull(lessonDates);
        assertEquals(GENERATED_DATES_FOR_FIXED_DURATION_LESSONS_EXPECTED_SIZE, lessonDates.size());
    }
}