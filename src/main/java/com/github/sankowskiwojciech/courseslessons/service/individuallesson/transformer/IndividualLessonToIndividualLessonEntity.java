package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonToIndividualLessonEntity implements Function<IndividualLesson, IndividualLessonEntity> {

    private static final IndividualLessonToIndividualLessonEntity INSTANCE = new IndividualLessonToIndividualLessonEntity();

    @Override
    public IndividualLessonEntity apply(IndividualLesson individualLesson) {
        return IndividualLessonEntity.builder()
                .title(individualLesson.getTitle())
                .startDateOfLesson(individualLesson.getStartDateOfLesson())
                .endDateOfLesson(individualLesson.getEndDateOfLesson())
                .description(individualLesson.getDescription())
                .organizationEntity(individualLesson.getOrganizationEntity())
                .tutorEntity(individualLesson.getTutorEntity())
                .studentEntity(individualLesson.getStudentEntity())
                .creationDateTime(LocalDateTime.now())
                .build();
    }

    public static IndividualLessonToIndividualLessonEntity getInstance() {
        return INSTANCE;
    }
}
