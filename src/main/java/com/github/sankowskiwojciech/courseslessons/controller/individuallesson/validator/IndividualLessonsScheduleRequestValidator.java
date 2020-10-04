package com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator;

import com.github.sankowskiwojciech.courseslessons.model.exception.InvalidRequestBodyDetailedException;
import com.github.sankowskiwojciech.courseslessons.model.exception.lesson.InvalidBeginningOrEndLessonsDateDetailedException;
import com.github.sankowskiwojciech.courseslessons.model.exception.lesson.InvalidLessonTimesDetailedException;
import com.github.sankowskiwojciech.courseslessons.model.exception.lesson.InvalidLessonsDurationDetailedException;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.courseslessons.model.lesson.DayOfWeekWithTimes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonsScheduleRequestValidator {

    public static void validateIndividualLessonsScheduleRequest(IndividualLessonsScheduleRequest individualLessonsScheduleRequest) {
        validateMandatoryFields(individualLessonsScheduleRequest);
        validateLessonsTimes(individualLessonsScheduleRequest.getLessonsDaysOfWeekWithTimes());
        validateMandatoryFieldsDependingOnScheduleType(individualLessonsScheduleRequest);
    }

    private static void validateMandatoryFields(IndividualLessonsScheduleRequest individualLessonsScheduleRequest) {
        if (individualLessonsScheduleRequest.getBeginningDate() == null
                || individualLessonsScheduleRequest.getScheduleType() == null
                || individualLessonsScheduleRequest.getLessonsDaysOfWeekWithTimes() == null
                || individualLessonsScheduleRequest.getLessonsDaysOfWeekWithTimes().isEmpty()
                || StringUtils.isAnyBlank(individualLessonsScheduleRequest.getStudentId(), individualLessonsScheduleRequest.getTutorId(), individualLessonsScheduleRequest.getSubdomainName())) {
            throw new InvalidRequestBodyDetailedException();
        }
    }

    private static void validateLessonsTimes(List<DayOfWeekWithTimes> lessonsTimes) {
        lessonsTimes.forEach(dayOfWeekWithTimes -> {
            Duration durationOfLesson = Duration.between(dayOfWeekWithTimes.getStartTime(), dayOfWeekWithTimes.getEndTime());
            if (durationOfLesson.isNegative() || durationOfLesson.isZero()) {
                throw new InvalidLessonTimesDetailedException();
            }
        });
    }

    private static void validateMandatoryFieldsDependingOnScheduleType(IndividualLessonsScheduleRequest individualLessonsScheduleRequest) {
        switch (individualLessonsScheduleRequest.getScheduleType()) {
            case FIXED_DURATION_LESSONS:
                validateMandatoryFieldsWhenScheduleTypeIsFixedDurationLessons(individualLessonsScheduleRequest.getAllLessonsDurationInMinutes());
                break;
            case FIXED_DATES_LESSONS:
                validateMandatoryFieldsWhenScheduleTypeIsFixedDatesLessons(individualLessonsScheduleRequest.getBeginningDate(), individualLessonsScheduleRequest.getEndDate());
                break;
        }
    }

    private static void validateMandatoryFieldsWhenScheduleTypeIsFixedDurationLessons(Long allLessonsDurationInMinutes) {
        if (allLessonsDurationInMinutes == null || allLessonsDurationInMinutes <= 0) {
            throw new InvalidLessonsDurationDetailedException();
        }
    }

    private static void validateMandatoryFieldsWhenScheduleTypeIsFixedDatesLessons(LocalDate beginningDate, LocalDate endDate) {
        if (beginningDate == null || endDate == null || !endDate.isAfter(beginningDate)) {
            throw new InvalidBeginningOrEndLessonsDateDetailedException();
        }
    }
}
