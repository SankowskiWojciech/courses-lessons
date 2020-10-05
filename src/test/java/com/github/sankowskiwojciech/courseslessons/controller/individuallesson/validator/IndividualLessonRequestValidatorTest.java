package com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidLessonDatesException;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequest;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonRequestStub;
import org.junit.Test;

import java.time.LocalDateTime;

public class IndividualLessonRequestValidatorTest {

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenIndividualLessonRequestIsNull() {
        //given
        IndividualLessonRequest individualLessonRequestStub = null;

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(individualLessonRequestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenStartDateOfLessonIsNull() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.createWithDatesOfLesson(null, LocalDateTime.now().plusHours(2));

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(individualLessonRequestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenEndDateOfLessonIsNull() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.createWithDatesOfLesson(LocalDateTime.now(), null);

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(individualLessonRequestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenTitleIsNull() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.createWithTitle(null);

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(individualLessonRequestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidLessonDatesException.class)
    public void shouldThrowInvalidLessonDatesExceptionWhenStartDateOrEndDateOfLessonIsInvalid() {
        //given
        LocalDateTime startDateOfLesson = LocalDateTime.now();
        LocalDateTime endDateOfLesson = LocalDateTime.now().minusHours(1);
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.createWithDatesOfLesson(startDateOfLesson, endDateOfLesson);

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(individualLessonRequestStub);

        //then exception is thrown
    }

    @Test
    public void shouldDoNothingWhenIndividualLessonRequestIsValid() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(individualLessonRequestStub);

        //then nothing happens
    }
}