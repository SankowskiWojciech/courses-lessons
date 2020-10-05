package com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidRequestBodyDetailedException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidBeginningOrEndLessonsDateDetailedException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidLessonTimesDetailedException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidLessonsDurationDetailedException;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.DayOfWeekWithTimes;
import com.github.sankowskiwojciech.courseslessons.stub.DayOfWeekWithTimesStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonsScheduleRequestStub;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class IndividualLessonsScheduleRequestValidatorTest {

    @Test(expected = InvalidRequestBodyDetailedException.class)
    public void shouldThrowInvalidRequestBodyExceptionWhenMandatoryFieldsAreMissing() {
        //given
        List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimesStub = Collections.emptyList();
        IndividualLessonsScheduleRequest individualLessonsScheduleRequestStub = IndividualLessonsScheduleRequestStub.createWithLessonsDaysOfWeekWithTimes(lessonsDaysOfWeekWithTimesStub);

        //when
        IndividualLessonsScheduleRequestValidator.validateIndividualLessonsScheduleRequest(individualLessonsScheduleRequestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidLessonTimesDetailedException.class)
    public void shouldThrowInvalidLessonTimesExceptionWhenLessonsTimesAreInvalid() {
        DayOfWeekWithTimes dayOfWeekWithTimesStub = DayOfWeekWithTimesStub.createInvalid();
        List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimesStub = Lists.newArrayList(dayOfWeekWithTimesStub);
        IndividualLessonsScheduleRequest individualLessonsScheduleRequestStub = IndividualLessonsScheduleRequestStub.createWithLessonsDaysOfWeekWithTimes(lessonsDaysOfWeekWithTimesStub);

        //when
        IndividualLessonsScheduleRequestValidator.validateIndividualLessonsScheduleRequest(individualLessonsScheduleRequestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidLessonsDurationDetailedException.class)
    public void shouldThrowInvalidAllLessonsDurationExceptionWhenMandatoryFieldsAreMissingAndScheduleTypeIsFixedDurationLessons() {
        //given
        final Long allLessonsDurationInMinutesStub = null;
        IndividualLessonsScheduleRequest individualLessonsScheduleRequestStub = IndividualLessonsScheduleRequestStub.createWithScheduleTypeFixedDurationLessons(allLessonsDurationInMinutesStub);

        //when
        IndividualLessonsScheduleRequestValidator.validateIndividualLessonsScheduleRequest(individualLessonsScheduleRequestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidBeginningOrEndLessonsDateDetailedException.class)
    public void shouldThrowInvalidBeginningOrEndLessonsDateExceptionWhenMandatoryFieldsAreMissingAndScheduleTypeIsFixedDatesLessons() {
        //given
        final LocalDate beginningDateStub = LocalDate.now();
        final LocalDate endDateStub = null;
        IndividualLessonsScheduleRequest individualLessonsScheduleRequestStub = IndividualLessonsScheduleRequestStub.createWithScheduleTypeFixedDatesLessons(beginningDateStub, endDateStub);

        //when
        IndividualLessonsScheduleRequestValidator.validateIndividualLessonsScheduleRequest(individualLessonsScheduleRequestStub);

        //then exception is thrown
    }

    @Test
    public void shouldDoNothingWhenScheduleRequestIsValid() {
        DayOfWeekWithTimes dayOfWeekWithTimesStub = DayOfWeekWithTimesStub.createValid();
        List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimesStub = Lists.newArrayList(dayOfWeekWithTimesStub);
        IndividualLessonsScheduleRequest individualLessonsScheduleRequestStub = IndividualLessonsScheduleRequestStub.createWithLessonsDaysOfWeekWithTimes(lessonsDaysOfWeekWithTimesStub);

        //when
        IndividualLessonsScheduleRequestValidator.validateIndividualLessonsScheduleRequest(individualLessonsScheduleRequestStub);

        //then nothing happens
    }
}