package com.github.sankowskiwojciech.courseslessons.controller.grouplesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidLessonDatesException;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import com.github.sankowskiwojciech.courseslessons.stub.GroupLessonRequestStub;
import org.junit.Test;

import java.time.LocalDateTime;

public class GroupLessonRequestValidatorTest {

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenGroupLessonRequestIsNull() {
        //given
        GroupLessonRequest groupLessonRequestStub = null;

        //when
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(groupLessonRequestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenStartDateOfLessonIsNull() {
        //given
        GroupLessonRequest groupLessonRequestStub = GroupLessonRequestStub.createWithDatesOfLesson(null, LocalDateTime.now().plusHours(2));

        //when
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(groupLessonRequestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenEndDateOfLessonIsNull() {
        //given
        GroupLessonRequest groupLessonRequestStub = GroupLessonRequestStub.createWithDatesOfLesson(LocalDateTime.now(), null);

        //when
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(groupLessonRequestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenTitleIsNull() {
        //given
        GroupLessonRequest groupLessonRequestStub = GroupLessonRequestStub.createWithTitle(null);

        //when
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(groupLessonRequestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidLessonDatesException.class)
    public void shouldThrowInvalidLessonDatesExceptionWhenStartDateOrEndDateOfLessonIsInvalid() {
        //given
        LocalDateTime startDateOfLesson = LocalDateTime.now();
        LocalDateTime endDateOfLesson = LocalDateTime.now().minusHours(1);
        GroupLessonRequest groupLessonRequestStub = GroupLessonRequestStub.createWithDatesOfLesson(startDateOfLesson, endDateOfLesson);

        //when
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(groupLessonRequestStub);

        //then exception is thrown
    }

    @Test
    public void shouldDoNothingWhenGroupLessonRequestIsValid() {
        //given
        GroupLessonRequest groupLessonRequestStub = GroupLessonRequestStub.create();

        //when
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(groupLessonRequestStub);

        //then nothing happens
    }
}