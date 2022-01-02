package com.github.sankowskiwojciech.courseslessons.service.grouplesson;

import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonsSchedule;

import java.util.List;

public interface GroupLessonsSchedulerService {
    List<GroupLessonResponse> scheduleGroupLessons(GroupLessonsSchedule schedule);
}
