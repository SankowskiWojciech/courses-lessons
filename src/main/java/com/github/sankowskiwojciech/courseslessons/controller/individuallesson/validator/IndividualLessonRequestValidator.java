package com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator;

import com.github.sankowskiwojciech.courseslessons.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.courseslessons.model.exception.lesson.InvalidLessonDatesException;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.request.IndividualLessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonRequestValidator {

    public static void validateCreateIndividualLessonRequest(IndividualLessonRequest individualLessonRequest) {
        validateIfMandatoryFieldsAreNotMissing(individualLessonRequest);
        validateIfStartDateAndEndDateOfLessonAreCorrect(individualLessonRequest);
    }

    private static void validateIfMandatoryFieldsAreNotMissing(IndividualLessonRequest individualLessonRequest) {
        if (individualLessonRequest == null || individualLessonRequest.getStartDateOfLesson() == null || individualLessonRequest.getEndDateOfLesson() == null || StringUtils.isAnyBlank(individualLessonRequest.getTitle(), individualLessonRequest.getSubdomainName(), individualLessonRequest.getTutorId(), individualLessonRequest.getStudentId())) {
            throw new InvalidRequestBodyException();
        }
    }

    private static void validateIfStartDateAndEndDateOfLessonAreCorrect(IndividualLessonRequest individualLessonRequest) {
        if (individualLessonRequest.getStartDateOfLesson().isAfter(individualLessonRequest.getEndDateOfLesson())) {
            throw new InvalidLessonDatesException();
        }
    }
}
