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
        GroupLessonRequest requestStub = null;

        //when
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenStartDateOfLessonIsNull() {
        //given
        GroupLessonRequest requestStub = GroupLessonRequestStub.createWithDatesOfLesson(null, LocalDateTime.now().plusHours(2));

        //when
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenEndDateOfLessonIsNull() {
        //given
        GroupLessonRequest requestStub = GroupLessonRequestStub.createWithDatesOfLesson(LocalDateTime.now(), null);

        //when
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenTitleIsNull() {
        //given
        GroupLessonRequest requestStub = GroupLessonRequestStub.createWithTitle(null);

        //when
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidLessonDatesException.class)
    public void shouldThrowInvalidLessonDatesExceptionWhenStartDateOrEndDateOfLessonIsInvalid() {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().minusHours(1);
        GroupLessonRequest requestStub = GroupLessonRequestStub.createWithDatesOfLesson(startDate, endDate);

        //when
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(requestStub);

        //then exception is thrown
    }

    @Test
    public void shouldDoNothingWhenGroupLessonRequestIsValid() {
        //given
        GroupLessonRequest requestStub = GroupLessonRequestStub.create();

        //when
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(requestStub);

        //then nothing happens
    }
}