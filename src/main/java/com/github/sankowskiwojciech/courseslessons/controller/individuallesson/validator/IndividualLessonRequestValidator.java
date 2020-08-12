package com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator;

import com.github.sankowskiwojciech.courseslessons.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonRequestValidator {

    public static void validateCreateIndividualLessonRequest(IndividualLessonRequest individualLessonRequest) {
        if (individualLessonRequest == null || individualLessonRequest.getDateOfLesson() == null || StringUtils.isAnyBlank(individualLessonRequest.getTitle(), individualLessonRequest.getSubdomainName(), individualLessonRequest.getTutorId(), individualLessonRequest.getStudentId())) {
            throw new InvalidRequestBodyException();
        }
    }
}
