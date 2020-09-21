package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonsSchedule;

import java.util.List;

public interface IndividualLessonsSchedulerService {
    List<IndividualLessonResponse> scheduleIndividualLessons(IndividualLessonsSchedule individualLessonsSchedule);
}
