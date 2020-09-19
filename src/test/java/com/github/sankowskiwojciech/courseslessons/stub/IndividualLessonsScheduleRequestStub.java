package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.courseslessons.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.courseslessons.model.lesson.DayOfWeekWithTimes;
import com.github.sankowskiwojciech.courseslessons.model.lesson.ScheduleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.assertj.core.util.Lists;

import java.time.LocalDate;
import java.util.List;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.ORGANIZATION_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.STUDENT_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonsScheduleRequestStub {

    public static IndividualLessonsScheduleRequest createWithLessonsDaysOfWeekWithTimes(List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes) {
        final LocalDate currentDate = LocalDate.now();
        return IndividualLessonsScheduleRequest.builder()
                .beginningDate(currentDate)
                .scheduleType(ScheduleType.ONE_YEAR_LENGTH_LESSONS)
                .lessonsDaysOfWeekWithTimes(lessonsDaysOfWeekWithTimes)
                .subdomainName(ORGANIZATION_EMAIL_ADDRESS_STUB)
                .tutorId(TUTOR_EMAIL_ADDRESS_STUB)
                .studentId(STUDENT_EMAIL_ADDRESS_STUB)
                .build();
    }

    public static IndividualLessonsScheduleRequest createWithScheduleTypeFixedDurationLessons(Long allLessonsDurationInMinutes) {
        final LocalDate currentDate = LocalDate.now();
        final DayOfWeekWithTimes dayOfWeekWithTimes = DayOfWeekWithTimesStub.createValid();
        final List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes = Lists.newArrayList(dayOfWeekWithTimes);
        return IndividualLessonsScheduleRequest.builder()
                .beginningDate(currentDate)
                .scheduleType(ScheduleType.FIXED_DURATION_LESSONS)
                .allLessonsDurationInMinutes(allLessonsDurationInMinutes)
                .lessonsDaysOfWeekWithTimes(lessonsDaysOfWeekWithTimes)
                .subdomainName(ORGANIZATION_EMAIL_ADDRESS_STUB)
                .tutorId(TUTOR_EMAIL_ADDRESS_STUB)
                .studentId(STUDENT_EMAIL_ADDRESS_STUB)
                .build();
    }

    public static IndividualLessonsScheduleRequest createWithScheduleTypeFixedDatesLessons(LocalDate beginningDate, LocalDate endDate) {
        final DayOfWeekWithTimes dayOfWeekWithTimes = DayOfWeekWithTimesStub.createValid();
        final List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes = Lists.newArrayList(dayOfWeekWithTimes);
        return IndividualLessonsScheduleRequest.builder()
                .beginningDate(beginningDate)
                .endDate(endDate)
                .scheduleType(ScheduleType.FIXED_DATES_LESSONS)
                .lessonsDaysOfWeekWithTimes(lessonsDaysOfWeekWithTimes)
                .subdomainName(ORGANIZATION_EMAIL_ADDRESS_STUB)
                .tutorId(TUTOR_EMAIL_ADDRESS_STUB)
                .studentId(STUDENT_EMAIL_ADDRESS_STUB)
                .build();
    }
}