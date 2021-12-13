package com.github.sankowskiwojciech.courseslessons.controller.grouplesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidBeginningOrEndLessonsDateException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidLessonTimesException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidLessonsDurationException;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.DayOfWeekWithTimes;
import com.github.sankowskiwojciech.coursestestlib.stub.DayOfWeekWithTimesStub;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupLessonsScheduleRequestStub;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class GroupLessonsScheduleRequestValidatorTest {

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyExceptionWhenMandatoryFieldsAreMissing() {
        //given
        List<DayOfWeekWithTimes> daysOfWeekWithTimes = Collections.emptyList();
        GroupLessonsScheduleRequest requestStub = GroupLessonsScheduleRequestStub.createWithLessonsDaysOfWeekWithTimes(daysOfWeekWithTimes);

        //when
        GroupLessonsScheduleRequestValidator.validateGroupLessonsScheduleRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidLessonTimesException.class)
    public void shouldThrowInvalidLessonTimesExceptionWhenLessonsTimesAreInvalid() {
        DayOfWeekWithTimes dayOfWeekWithTimesStub = DayOfWeekWithTimesStub.createInvalid();
        List<DayOfWeekWithTimes> daysOfWeekWithTimesStub = Lists.newArrayList(dayOfWeekWithTimesStub);
        GroupLessonsScheduleRequest requestStub = GroupLessonsScheduleRequestStub.createWithLessonsDaysOfWeekWithTimes(daysOfWeekWithTimesStub);

        //when
        GroupLessonsScheduleRequestValidator.validateGroupLessonsScheduleRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidLessonsDurationException.class)
    public void shouldThrowInvalidAllLessonsDurationExceptionWhenMandatoryFieldsAreMissingAndScheduleTypeIsFixedDurationLessons() {
        //given
        final Long durationOfAllLessonsInMinutesStub = null;
        GroupLessonsScheduleRequest requestStub = GroupLessonsScheduleRequestStub.createWithScheduleTypeFixedDurationLessons(durationOfAllLessonsInMinutesStub);

        //when
        GroupLessonsScheduleRequestValidator.validateGroupLessonsScheduleRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidBeginningOrEndLessonsDateException.class)
    public void shouldThrowInvalidBeginningOrEndLessonsDateExceptionWhenMandatoryFieldsAreMissingAndScheduleTypeIsFixedDatesLessons() {
        //given
        final LocalDate beginningDateStub = LocalDate.now();
        final LocalDate endDateStub = null;
        GroupLessonsScheduleRequest requestStub = GroupLessonsScheduleRequestStub.createWithScheduleTypeFixedDatesLessons(beginningDateStub, endDateStub);

        //when
        GroupLessonsScheduleRequestValidator.validateGroupLessonsScheduleRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyExceptionWhenGroupIdIsMissing() {
        String groupId = null;
        GroupLessonsScheduleRequest requestStub = GroupLessonsScheduleRequestStub.createWithGroupId(groupId);

        //when
        GroupLessonsScheduleRequestValidator.validateGroupLessonsScheduleRequest(requestStub);

        //then exception is thrown
    }

    @Test
    public void shouldDoNothingWhenScheduleRequestIsValid() {
        DayOfWeekWithTimes dayOfWeekWithTimesStub = DayOfWeekWithTimesStub.createValid();
        List<DayOfWeekWithTimes> daysOfWeekWithTimesStub = Lists.newArrayList(dayOfWeekWithTimesStub);
        GroupLessonsScheduleRequest requestStub = GroupLessonsScheduleRequestStub.createWithLessonsDaysOfWeekWithTimes(daysOfWeekWithTimesStub);

        //when
        GroupLessonsScheduleRequestValidator.validateGroupLessonsScheduleRequest(requestStub);

        //then nothing happens
    }
}