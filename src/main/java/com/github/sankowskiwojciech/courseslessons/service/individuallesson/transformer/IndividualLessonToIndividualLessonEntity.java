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
        IndividualLessonEntity entity = new IndividualLessonEntity();
        entity.setTitle(individualLesson.getTitle());
        entity.setStartDate(individualLesson.getStartDate());
        entity.setEndDate(individualLesson.getEndDate());
        entity.setDescription(individualLesson.getDescription());
        entity.setOrganizationEntity(individualLesson.getOrganizationEntity());
        entity.setTutorEntity(individualLesson.getTutorEntity());
        entity.setStudentEntity(individualLesson.getStudentEntity());
        entity.setCreationDateTime(LocalDateTime.now());
        return entity;
    }

    public static IndividualLessonToIndividualLessonEntity getInstance() {
        return INSTANCE;
    }
}
