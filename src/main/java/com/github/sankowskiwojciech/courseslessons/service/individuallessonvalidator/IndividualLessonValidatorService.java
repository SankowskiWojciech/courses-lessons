package com.github.sankowskiwojciech.courseslessons.service.individuallessonvalidator;

import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.request.IndividualLessonRequest;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.request.IndividualLessonsScheduleRequest;

public interface IndividualLessonValidatorService {

    IndividualLesson validateCreateIndividualLessonRequest(IndividualLessonRequest individualLessonRequest);

    IndividualLessonsSchedule validateIndividualLessonsScheduleRequest(IndividualLessonsScheduleRequest individualLessonsScheduleRequest);
}
