package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonCollisionValidatorService {
    void validateIfNewLessonDoesNotCollideWithExistingOnes(LocalDateTime startDate, LocalDateTime endDate, String tutorEmailAddress);

    void validateIfScheduledLessonsDoesNotCollideWithExistingOnes(List<LessonDates> generatedDates, String tutorEmailAddress);
}
