package com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidLessonDatesException;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequest;
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
        if (individualLessonRequest == null || individualLessonRequest.getStartDate() == null || individualLessonRequest.getEndDate() == null || StringUtils.isAnyBlank(individualLessonRequest.getTitle(), individualLessonRequest.getSubdomainAlias(), individualLessonRequest.getTutorId(), individualLessonRequest.getStudentId())) {
            throw new InvalidRequestBodyException();
        }
    }

    private static void validateIfStartDateAndEndDateOfLessonAreCorrect(IndividualLessonRequest individualLessonRequest) {
        if (individualLessonRequest.getStartDate().isAfter(individualLessonRequest.getEndDate())) {
            throw new InvalidLessonDatesException();
        }
    }
}
