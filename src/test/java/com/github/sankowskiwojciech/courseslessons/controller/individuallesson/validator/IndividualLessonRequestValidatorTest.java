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
        IndividualLessonRequest requestStub = null;

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenStartDateOfLessonIsNull() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.createWithDatesOfLesson(null, LocalDateTime.now().plusHours(2));

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenEndDateOfLessonIsNull() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.createWithDatesOfLesson(LocalDateTime.now(), null);

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyWhenTitleIsNull() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.createWithTitle(null);

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidLessonDatesException.class)
    public void shouldThrowInvalidLessonDatesExceptionWhenStartDateOrEndDateOfLessonIsInvalid() {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().minusHours(1);
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.createWithDatesOfLesson(startDate, endDate);

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(requestStub);

        //then exception is thrown
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyExceptionWhenStudentIdIsNull() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.createWithStudentId(null);

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(requestStub);

        //then exception is thrown
    }

    @Test
    public void shouldDoNothingWhenIndividualLessonRequestIsValid() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(requestStub);

        //then nothing happens
    }
}