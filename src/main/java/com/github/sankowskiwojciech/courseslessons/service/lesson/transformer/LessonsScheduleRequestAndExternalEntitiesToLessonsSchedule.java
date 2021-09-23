package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonsScheduleRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonsScheduleRequestAndExternalEntitiesToLessonsSchedule {

    public static LessonsSchedule transform(LessonsScheduleRequest request, OrganizationEntity organization, TutorEntity tutor) {
        return new LessonsSchedule(request.getBeginningDate(), request.getEndDate(), request.getScheduleType(), request.getAllLessonsDurationInMinutes(), request.getDaysOfWeekWithTimes(), request.getTitles(), organization, tutor);
    }
}
