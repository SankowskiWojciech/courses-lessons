package com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidBeginningOrEndLessonsDateException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidLessonTimesException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidLessonsDurationException;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.DayOfWeekWithTimes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonsScheduleRequestValidator {
    public static void validateIndividualLessonsScheduleRequest(IndividualLessonsScheduleRequest request) {
        validateMandatoryFields(request);
        validateLessonsTimes(request.getDaysOfWeekWithTimes());
        validateMandatoryFieldsDependingOnScheduleType(request);
    }

    private static void validateMandatoryFields(IndividualLessonsScheduleRequest request) {
        if (request.getBeginningDate() == null
                || request.getScheduleType() == null
                || request.getDaysOfWeekWithTimes() == null
                || request.getDaysOfWeekWithTimes().isEmpty()
                || StringUtils.isAnyBlank(request.getStudentId(), request.getTutorId(), request.getSubdomainAlias())) {
            throw new InvalidRequestBodyException();
        }
    }

    private static void validateLessonsTimes(List<DayOfWeekWithTimes> lessonsTimes) {
        lessonsTimes.forEach(dayOfWeekWithTimes -> {
            Duration durationOfLesson = Duration.between(dayOfWeekWithTimes.getStartTime(), dayOfWeekWithTimes.getEndTime());
            if (durationOfLesson.isNegative() || durationOfLesson.isZero()) {
                throw new InvalidLessonTimesException();
            }
        });
    }

    private static void validateMandatoryFieldsDependingOnScheduleType(IndividualLessonsScheduleRequest request) {
        switch (request.getScheduleType()) {
            case FIXED_DURATION_LESSONS:
                validateMandatoryFieldsWhenScheduleTypeIsFixedDurationLessons(request.getAllLessonsDurationInMinutes());
                break;
            case FIXED_DATES_LESSONS:
                validateMandatoryFieldsWhenScheduleTypeIsFixedDatesLessons(request.getBeginningDate(), request.getEndDate());
                break;
        }
    }

    private static void validateMandatoryFieldsWhenScheduleTypeIsFixedDurationLessons(Long durationOfAllLessonsInMinutes) {
        if (durationOfAllLessonsInMinutes == null || durationOfAllLessonsInMinutes <= 0) {
            throw new InvalidLessonsDurationException();
        }
    }

    private static void validateMandatoryFieldsWhenScheduleTypeIsFixedDatesLessons(LocalDate beginningDate, LocalDate endDate) {
        if (beginningDate == null || endDate == null || !endDate.isAfter(beginningDate)) {
            throw new InvalidBeginningOrEndLessonsDateException();
        }
    }
}
