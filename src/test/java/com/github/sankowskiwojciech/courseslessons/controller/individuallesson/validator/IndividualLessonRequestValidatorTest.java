package com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator;

import com.github.sankowskiwojciech.courseslessons.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonRequest;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonRequestStub;
import org.junit.Test;

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
    public void shouldThrowInvalidRequestBodyWhenDateOfLessonIsNull() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.createWithDateOfLesson(null);

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

    @Test
    public void shouldDoNothingWhenIndividualLessonRequestIsValid() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();

        //when
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(individualLessonRequestStub);

        //then nothing happens
    }
}