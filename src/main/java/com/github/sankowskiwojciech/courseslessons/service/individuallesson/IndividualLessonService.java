package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonResponse;

public interface IndividualLessonService {

    IndividualLessonResponse createIndividualLesson(IndividualLesson individualLesson);
}
