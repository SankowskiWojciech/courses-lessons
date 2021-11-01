package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.DayOfWeekWithTimes;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.ScheduleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.assertj.core.util.Lists;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonsScheduleStub {

    private static final long ALL_LESSONS_DURATION_IN_MINUTES_STUB = 240;

    public static IndividualLessonsSchedule createWithLessonsTitles(List<String> lessonsTitles) {
        return new IndividualLessonsSchedule(LocalDate.now(), null, ScheduleType.FIXED_DATES_LESSONS, null, createDayOfWeekWithTimes(), lessonsTitles, SubdomainEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
    }

    public static IndividualLessonsSchedule createWithScheduleType(ScheduleType scheduleType) {
        switch (scheduleType) {
            case ONE_YEAR_LENGTH_LESSONS:
                return createForScheduleTypeOneYearLengthLessons();
            case FIXED_DATES_LESSONS:
                return createForScheduleTypeFixedDatesLessons();
            default:
                return createForScheduleTypeFixedDurationLessons();
        }
    }

    private static IndividualLessonsSchedule createForScheduleTypeOneYearLengthLessons() {
        return new IndividualLessonsSchedule(LocalDate.now(), null, ScheduleType.ONE_YEAR_LENGTH_LESSONS, null, createDayOfWeekWithTimes(), null, SubdomainEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
    }

    private static IndividualLessonsSchedule createForScheduleTypeFixedDatesLessons() {
        final LocalDate currentDate = LocalDate.now();
        return new IndividualLessonsSchedule(currentDate, currentDate.plusMonths(3), ScheduleType.FIXED_DATES_LESSONS, null, createDayOfWeekWithTimes(), null, SubdomainEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
    }

    private static IndividualLessonsSchedule createForScheduleTypeFixedDurationLessons() {
        return new IndividualLessonsSchedule(LocalDate.now(), null, ScheduleType.FIXED_DURATION_LESSONS, ALL_LESSONS_DURATION_IN_MINUTES_STUB, createDayOfWeekWithTimes(), null, SubdomainEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
    }

    private static List<DayOfWeekWithTimes> createDayOfWeekWithTimes() {
        final DayOfWeekWithTimes dayOfWeekWithTimes = DayOfWeekWithTimesStub.createValid();
        return Lists.newArrayList(dayOfWeekWithTimes);
    }
}
