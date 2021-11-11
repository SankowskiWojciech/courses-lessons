package com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidBeginningOrEndLessonsDateException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidLessonTimesException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidLessonsDurationException;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.DayOfWeekWithTimes;
import com.github.sankowskiwojciech.coursestestlib.stub.DayOfWeekWithTimesStub;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonsScheduleRequestStub;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class IndividualLessonsScheduleRequestValidatorTest {

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyExceptionWhenMandatoryFieldsAreMissing() {
        //given
        List<DayOfWeekWithTimes> daysOfWeekWithTimes = Collections.emptyList();
        IndividualLessonsScheduleRequest requestStub = IndividualLessonsScheduleRequestStub.createWithLessonsDaysOfWeekWithTimes(daysOfWeekWithTimes);

        //when
        IndividualLessonsScheduleRequestValidator.validateIndividualLessonsScheduleRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidLessonTimesException.class)
    public void shouldThrowInvalidLessonTimesExceptionWhenLessonsTimesAreInvalid() {
        DayOfWeekWithTimes dayOfWeekWithTimesStub = DayOfWeekWithTimesStub.createInvalid();
        List<DayOfWeekWithTimes> daysOfWeekWithTimesStub = Lists.newArrayList(dayOfWeekWithTimesStub);
        IndividualLessonsScheduleRequest requestStub = IndividualLessonsScheduleRequestStub.createWithLessonsDaysOfWeekWithTimes(daysOfWeekWithTimesStub);

        //when
        IndividualLessonsScheduleRequestValidator.validateIndividualLessonsScheduleRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidLessonsDurationException.class)
    public void shouldThrowInvalidAllLessonsDurationExceptionWhenMandatoryFieldsAreMissingAndScheduleTypeIsFixedDurationLessons() {
        //given
        final Long durationOfAllLessonsInMinutesStub = null;
        IndividualLessonsScheduleRequest requestStub = IndividualLessonsScheduleRequestStub.createWithScheduleTypeFixedDurationLessons(durationOfAllLessonsInMinutesStub);

        //when
        IndividualLessonsScheduleRequestValidator.validateIndividualLessonsScheduleRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidBeginningOrEndLessonsDateException.class)
    public void shouldThrowInvalidBeginningOrEndLessonsDateExceptionWhenMandatoryFieldsAreMissingAndScheduleTypeIsFixedDatesLessons() {
        //given
        final LocalDate beginningDateStub = LocalDate.now();
        final LocalDate endDateStub = null;
        IndividualLessonsScheduleRequest requestStub = IndividualLessonsScheduleRequestStub.createWithScheduleTypeFixedDatesLessons(beginningDateStub, endDateStub);

        //when
        IndividualLessonsScheduleRequestValidator.validateIndividualLessonsScheduleRequest(requestStub);

        //then exception is thrown
    }

    @Test
    public void shouldDoNothingWhenScheduleRequestIsValid() {
        DayOfWeekWithTimes dayOfWeekWithTimesStub = DayOfWeekWithTimesStub.createValid();
        List<DayOfWeekWithTimes> daysOfWeekWithTimesStub = Lists.newArrayList(dayOfWeekWithTimesStub);
        IndividualLessonsScheduleRequest requestStub = IndividualLessonsScheduleRequestStub.createWithLessonsDaysOfWeekWithTimes(daysOfWeekWithTimesStub);

        //when
        IndividualLessonsScheduleRequestValidator.validateIndividualLessonsScheduleRequest(requestStub);

        //then nothing happens
    }
}