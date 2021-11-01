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
    public IndividualLessonEntity apply(IndividualLesson lesson) {
        IndividualLessonEntity entity = new IndividualLessonEntity();
        entity.setTitle(lesson.getTitle());
        entity.setStartDate(lesson.getStartDate());
        entity.setEndDate(lesson.getEndDate());
        entity.setDescription(lesson.getDescription());
        entity.setSubdomainEntity(lesson.getSubdomainEntity());
        entity.setTutorEntity(lesson.getTutorEntity());
        entity.setStudentEntity(lesson.getStudentEntity());
        entity.setCreationDateTime(LocalDateTime.now());
        return entity;
    }

    public static IndividualLessonToIndividualLessonEntity getInstance() {
        return INSTANCE;
    }
}
