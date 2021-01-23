package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.DayOfWeekWithTimes;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.ScheduleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.assertj.core.util.Lists;

import java.time.LocalDate;
import java.util.List;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonsScheduleRequestStub {

    public static IndividualLessonsScheduleRequest createWithLessonsDaysOfWeekWithTimes(List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes) {
        return new IndividualLessonsScheduleRequest(LocalDate.now(), null, ScheduleType.ONE_YEAR_LENGTH_LESSONS, null, lessonsDaysOfWeekWithTimes, null, ORGANIZATION_EMAIL_ADDRESS_STUB, TUTOR_EMAIL_ADDRESS_STUB, STUDENT_EMAIL_ADDRESS_STUB);
    }

    public static IndividualLessonsScheduleRequest createWithScheduleTypeFixedDurationLessons(Long allLessonsDurationInMinutes) {
        final DayOfWeekWithTimes dayOfWeekWithTimes = DayOfWeekWithTimesStub.createValid();
        final List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes = Lists.newArrayList(dayOfWeekWithTimes);
        return new IndividualLessonsScheduleRequest(LocalDate.now(), null, ScheduleType.FIXED_DURATION_LESSONS, allLessonsDurationInMinutes, lessonsDaysOfWeekWithTimes, null, ORGANIZATION_EMAIL_ADDRESS_STUB, TUTOR_EMAIL_ADDRESS_STUB, STUDENT_EMAIL_ADDRESS_STUB);
    }

    public static IndividualLessonsScheduleRequest createWithScheduleTypeFixedDatesLessons(LocalDate beginningDate, LocalDate endDate) {
        final DayOfWeekWithTimes dayOfWeekWithTimes = DayOfWeekWithTimesStub.createValid();
        final List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes = Lists.newArrayList(dayOfWeekWithTimes);
        return new IndividualLessonsScheduleRequest(beginningDate, endDate, ScheduleType.FIXED_DATES_LESSONS, null, lessonsDaysOfWeekWithTimes, null, ORGANIZATION_EMAIL_ADDRESS_STUB, TUTOR_EMAIL_ADDRESS_STUB, STUDENT_EMAIL_ADDRESS_STUB);
    }
}