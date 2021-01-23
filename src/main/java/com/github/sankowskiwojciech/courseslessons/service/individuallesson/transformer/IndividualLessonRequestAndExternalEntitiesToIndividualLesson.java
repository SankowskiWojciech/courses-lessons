package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonRequestAndExternalEntitiesToIndividualLesson {

    public static IndividualLesson transform(IndividualLessonRequest individualLessonRequest, OrganizationEntity organizationEntity, TutorEntity tutorEntity, StudentEntity studentEntity) {
        return new IndividualLesson(individualLessonRequest.getTitle(), individualLessonRequest.getStartDateOfLesson(), individualLessonRequest.getEndDateOfLesson(), individualLessonRequest.getDescription(), organizationEntity, tutorEntity, individualLessonRequest.getFilesIds(), studentEntity);
    }
}
