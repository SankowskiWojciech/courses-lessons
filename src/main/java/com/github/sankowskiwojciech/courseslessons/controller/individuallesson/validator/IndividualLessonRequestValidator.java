package com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequest;
import com.github.sankowskiwojciech.courseslessons.controller.lesson.validator.LessonRequestValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonRequestValidator {
    public static void validateCreateIndividualLessonRequest(IndividualLessonRequest request) {
        LessonRequestValidator.validateLessonRequest(request);
        validateIfMandatoryFieldsAreNotMissing(request);
    }

    private static void validateIfMandatoryFieldsAreNotMissing(IndividualLessonRequest request) {
        if (StringUtils.isBlank(request.getStudentId())) {
            throw new InvalidRequestBodyException();
        }
    }
}
