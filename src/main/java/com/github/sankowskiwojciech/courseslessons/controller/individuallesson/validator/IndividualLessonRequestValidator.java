package com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator;

import com.github.sankowskiwojciech.courseslessons.model.exception.InvalidRequestBodyDetailedException;
import com.github.sankowskiwojciech.courseslessons.model.exception.lesson.InvalidLessonDatesDetailedException;
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
            throw new InvalidRequestBodyDetailedException();
        }
    }

    private static void validateIfStartDateAndEndDateOfLessonAreCorrect(IndividualLessonRequest individualLessonRequest) {
        if (individualLessonRequest.getStartDateOfLesson().isAfter(individualLessonRequest.getEndDateOfLesson())) {
            throw new InvalidLessonDatesDetailedException();
        }
    }
}
