package com.github.sankowskiwojciech.courseslessons.controller.grouplesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import com.github.sankowskiwojciech.courseslessons.controller.lesson.validator.LessonRequestValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupLessonRequestValidator {
    public static void validateCreateGroupLessonRequest(GroupLessonRequest request) {
        LessonRequestValidator.validateLessonRequest(request);
        validateIfMandatoryFieldsAreNotMissing(request);
    }

    private static void validateIfMandatoryFieldsAreNotMissing(GroupLessonRequest request) {
        if (StringUtils.isBlank(request.getGroupId())) {
            throw new InvalidRequestBodyException();
        }
    }
}
