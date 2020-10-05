package com.github.sankowskiwojciech.courseslessons.service.lessonvalidator;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonCollisionValidatorService {
    void validateIfNewLessonDoesNotCollideWithExistingOnes(LocalDateTime startDateOfLesson, LocalDateTime endDateOfLesson, String tutorEmailAddress, String organizationEmailAddress);

    void validateIfScheduledLessonsDoesNotCollideWithExistingOnes(List<LessonDates> generatedLessonsDates, String tutorEmailAddress, String organizationEmailAddress);
}
