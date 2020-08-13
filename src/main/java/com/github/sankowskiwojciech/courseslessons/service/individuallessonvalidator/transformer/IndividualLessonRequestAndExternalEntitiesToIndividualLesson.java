package com.github.sankowskiwojciech.courseslessons.service.individuallessonvalidator.transformer;

import com.github.sankowskiwojciech.courseslessons.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.request.IndividualLessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonRequestAndExternalEntitiesToIndividualLesson {

    public static IndividualLesson transform(IndividualLessonRequest individualLessonRequest, OrganizationEntity organizationEntity, TutorEntity tutorEntity, StudentEntity studentEntity) {
        return IndividualLesson.builder()
                .title(individualLessonRequest.getTitle())
                .dateOfLesson(individualLessonRequest.getDateOfLesson())
                .description(individualLessonRequest.getDescription())
                .organizationEntity(organizationEntity)
                .tutorEntity(tutorEntity)
                .studentEntity(studentEntity)
                .build();
    }
}
