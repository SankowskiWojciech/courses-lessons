package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLesson;
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
                .dateOfLesson(individualLesson.getDateOfLesson())
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
