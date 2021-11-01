package com.github.sankowskiwojciech.courseslessons.controller.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidLessonDatesException;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonRequestValidator {
    public static void validateLessonRequest(LessonRequest request) {
        validateIfMandatoryFieldsAreNotMissing(request);
        validateIfStartDateAndEndDateOfLessonAreCorrect(request);
    }

    private static void validateIfMandatoryFieldsAreNotMissing(LessonRequest request) {
        if (request == null || request.getStartDate() == null || request.getEndDate() == null || StringUtils.isAnyBlank(request.getTitle(), request.getSubdomainAlias())) {
            throw new InvalidRequestBodyException();
        }
    }

    private static void validateIfStartDateAndEndDateOfLessonAreCorrect(LessonRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new InvalidLessonDatesException();
        }
    }
}
