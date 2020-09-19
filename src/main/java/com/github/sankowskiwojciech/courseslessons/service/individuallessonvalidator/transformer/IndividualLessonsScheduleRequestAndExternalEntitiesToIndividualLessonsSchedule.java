package com.github.sankowskiwojciech.courseslessons.service.individuallessonvalidator.transformer;

import com.github.sankowskiwojciech.courseslessons.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.request.IndividualLessonsScheduleRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonsScheduleRequestAndExternalEntitiesToIndividualLessonsSchedule {

    public static IndividualLessonsSchedule transform(IndividualLessonsScheduleRequest individualLessonsScheduleRequest, OrganizationEntity organizationEntity, TutorEntity tutorEntity, StudentEntity studentEntity) {
        return IndividualLessonsSchedule.builder()
                .beginningDate(individualLessonsScheduleRequest.getBeginningDate())
                .endDate(individualLessonsScheduleRequest.getEndDate())
                .scheduleType(individualLessonsScheduleRequest.getScheduleType())
                .allLessonsDurationInMinutes(individualLessonsScheduleRequest.getAllLessonsDurationInMinutes())
                .lessonsDaysOfWeekWithTimes(individualLessonsScheduleRequest.getLessonsDaysOfWeekWithTimes())
                .lessonTitles(individualLessonsScheduleRequest.getLessonTitles())
                .organizationEntity(organizationEntity)
                .tutorEntity(tutorEntity)
                .studentEntity(studentEntity)
                .build();
    }
}
