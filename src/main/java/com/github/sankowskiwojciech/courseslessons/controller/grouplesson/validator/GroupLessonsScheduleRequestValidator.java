package com.github.sankowskiwojciech.courseslessons.controller.grouplesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonsScheduleRequest;
import com.github.sankowskiwojciech.courseslessons.controller.lesson.validator.LessonsScheduleRequestValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupLessonsScheduleRequestValidator {
    public static void validateGroupLessonsScheduleRequest(GroupLessonsScheduleRequest request) {
        LessonsScheduleRequestValidator.validateLessonsScheduleRequest(request);
        validateMandatoryFields(request);
    }

    private static void validateMandatoryFields(GroupLessonsScheduleRequest request) {
        if (StringUtils.isBlank(request.getGroupId())) {
            throw new InvalidRequestBodyException();
        }
    }
}
