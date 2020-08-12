package com.github.sankowskiwojciech.courseslessons.service.individuallessonvalidator;

import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonRequest;

public interface IndividualLessonValidatorService {

    IndividualLesson validateCreateIndividualLessonRequest(IndividualLessonRequest individualLessonRequest);
}
