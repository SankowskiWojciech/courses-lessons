package com.github.sankowskiwojciech.courseslessons.controller.grouplesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.exception.InvalidRequestBodyException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.InvalidLessonDatesException;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupLessonRequestValidator {
    public static void validateCreateGroupLessonRequest(GroupLessonRequest groupLessonRequest) {
        validateIfMandatoryFieldsAreNotMissing(groupLessonRequest);
        validateIfStartDateAndEndDateOfLessonAreCorrect(groupLessonRequest);
    }

    private static void validateIfMandatoryFieldsAreNotMissing(GroupLessonRequest groupLessonRequest) {
        if (groupLessonRequest == null || groupLessonRequest.getStartDate() == null || groupLessonRequest.getEndDate() == null || StringUtils.isAnyBlank(groupLessonRequest.getTitle(), groupLessonRequest.getSubdomainAlias(), groupLessonRequest.getTutorId(), groupLessonRequest.getGroupId())) {
            throw new InvalidRequestBodyException();
        }
    }

    private static void validateIfStartDateAndEndDateOfLessonAreCorrect(GroupLessonRequest groupLessonRequest) {
        if (groupLessonRequest.getStartDate().isAfter(groupLessonRequest.getEndDate())) {
            throw new InvalidLessonDatesException();
        }
    }
}
