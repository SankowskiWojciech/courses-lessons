package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.courseslessons.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLesson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_DESCRIPTION_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_TITLE_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonStub {

    public static IndividualLesson createWithExternalEntities(OrganizationEntity organizationEntity, TutorEntity tutorEntity, StudentEntity studentEntity) {
        return IndividualLesson.builder()
                .title(LESSON_TITLE_STUB)
                .dateOfLesson(LocalDateTime.now().plusMonths(1))
                .description(LESSON_DESCRIPTION_STUB)
                .organizationEntity(organizationEntity)
                .tutorEntity(tutorEntity)
                .studentEntity(studentEntity)
                .build();
    }
}
