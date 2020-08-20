package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.tutor.TutorEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.INDIVIDUAL_LESSON_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_DESCRIPTION_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_TITLE_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonEntityStub {

    public static IndividualLessonEntity createWithExternalEntities(OrganizationEntity organizationEntity, TutorEntity tutorEntity, StudentEntity studentEntity) {
        return IndividualLessonEntity.builder()
                .lessonId(INDIVIDUAL_LESSON_ID_STUB)
                .title(LESSON_TITLE_STUB)
                .dateOfLesson(LocalDateTime.now().plusMonths(1))
                .description(LESSON_DESCRIPTION_STUB)
                .organizationEntity(organizationEntity)
                .tutorEntity(tutorEntity)
                .studentEntity(studentEntity)
                .creationDateTime(LocalDateTime.now())
                .build();
    }

    public static IndividualLessonEntity create() {
        return IndividualLessonEntity.builder()
                .lessonId(INDIVIDUAL_LESSON_ID_STUB)
                .title(LESSON_TITLE_STUB)
                .dateOfLesson(LocalDateTime.now().plusMonths(1))
                .description(LESSON_DESCRIPTION_STUB)
                .organizationEntity(OrganizationEntityStub.create())
                .tutorEntity(TutorEntityStub.create())
                .studentEntity(StudentEntityStub.create())
                .creationDateTime(LocalDateTime.now().minusHours(2))
                .build();
    }
}
