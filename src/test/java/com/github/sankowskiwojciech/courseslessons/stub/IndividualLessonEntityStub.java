package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.INDIVIDUAL_LESSON_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_DESCRIPTION_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_TITLE_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonEntityStub {

    public static IndividualLessonEntity createWithExternalEntities(OrganizationEntity organizationEntity, TutorEntity tutorEntity, StudentEntity studentEntity) {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return IndividualLessonEntity.builder()
                .lessonId(INDIVIDUAL_LESSON_ID_STUB)
                .title(LESSON_TITLE_STUB)
                .startDateOfLesson(currentDateTime.plusHours(1))
                .endDateOfLesson(currentDateTime.plusHours(3))
                .description(LESSON_DESCRIPTION_STUB)
                .organizationEntity(organizationEntity)
                .tutorEntity(tutorEntity)
                .studentEntity(studentEntity)
                .creationDateTime(currentDateTime)
                .build();
    }

    public static IndividualLessonEntity createWithDatesOfLesson(LocalDateTime startDateOfLesson, LocalDateTime endDateOfLesson) {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return IndividualLessonEntity.builder()
                .lessonId(INDIVIDUAL_LESSON_ID_STUB)
                .title(LESSON_TITLE_STUB)
                .startDateOfLesson(startDateOfLesson)
                .endDateOfLesson(endDateOfLesson)
                .description(LESSON_DESCRIPTION_STUB)
                .organizationEntity(OrganizationEntityStub.create())
                .tutorEntity(TutorEntityStub.create())
                .studentEntity(StudentEntityStub.create())
                .creationDateTime(currentDateTime.minusMonths(2))
                .build();
    }
}
