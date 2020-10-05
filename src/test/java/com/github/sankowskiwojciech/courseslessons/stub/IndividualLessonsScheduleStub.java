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
        final LocalDate currentDate = LocalDate.now();
        return IndividualLessonsSchedule.builder()
                .beginningDate(currentDate)
                .scheduleType(ScheduleType.FIXED_DURATION_LESSONS)
                .lessonsDaysOfWeekWithTimes(createDayOfWeekWithTimes())
                .lessonTitles(lessonsTitles)
                .organizationEntity(OrganizationEntityStub.create())
                .tutorEntity(TutorEntityStub.create())
                .studentEntity(StudentEntityStub.create())
                .build();
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
        final LocalDate currentDate = LocalDate.now();
        return IndividualLessonsSchedule.builder()
                .beginningDate(currentDate)
                .scheduleType(ScheduleType.ONE_YEAR_LENGTH_LESSONS)
                .lessonsDaysOfWeekWithTimes(createDayOfWeekWithTimes())
                .organizationEntity(OrganizationEntityStub.create())
                .tutorEntity(TutorEntityStub.create())
                .studentEntity(StudentEntityStub.create())
                .build();
    }

    private static IndividualLessonsSchedule createForScheduleTypeFixedDatesLessons() {
        final LocalDate currentDate = LocalDate.now();
        return IndividualLessonsSchedule.builder()
                .beginningDate(currentDate)
                .endDate(currentDate.plusMonths(3))
                .scheduleType(ScheduleType.FIXED_DATES_LESSONS)
                .lessonsDaysOfWeekWithTimes(createDayOfWeekWithTimes())
                .organizationEntity(OrganizationEntityStub.create())
                .tutorEntity(TutorEntityStub.create())
                .studentEntity(StudentEntityStub.create())
                .build();
    }

    private static IndividualLessonsSchedule createForScheduleTypeFixedDurationLessons() {
        final LocalDate currentDate = LocalDate.now();
        return IndividualLessonsSchedule.builder()
                .beginningDate(currentDate)
                .scheduleType(ScheduleType.FIXED_DURATION_LESSONS)
                .allLessonsDurationInMinutes(ALL_LESSONS_DURATION_IN_MINUTES_STUB)
                .lessonsDaysOfWeekWithTimes(createDayOfWeekWithTimes())
                .organizationEntity(OrganizationEntityStub.create())
                .tutorEntity(TutorEntityStub.create())
                .studentEntity(StudentEntityStub.create())
                .build();
    }

    private static List<DayOfWeekWithTimes> createDayOfWeekWithTimes() {
        final DayOfWeekWithTimes dayOfWeekWithTimes = DayOfWeekWithTimesStub.createValid();
        return Lists.newArrayList(dayOfWeekWithTimes);
    }
}
