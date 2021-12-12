package com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.courseslessons.controller.lesson.validator.LessonsScheduleRequestValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonsScheduleRequestValidator {
    public static void validateIndividualLessonsScheduleRequest(IndividualLessonsScheduleRequest request) {
        LessonsScheduleRequestValidator.validateLessonsScheduleRequest(request);
        validateMandatoryFields(request);
    }

    private static void validateMandatoryFields(IndividualLessonsScheduleRequest request) {
        if (StringUtils.isBlank(request.getStudentId())) {
            throw new InvalidRequestBodyException();
        }
    }
}
