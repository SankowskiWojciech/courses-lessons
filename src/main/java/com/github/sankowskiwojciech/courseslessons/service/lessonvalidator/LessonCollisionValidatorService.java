package com.github.sankowskiwojciech.courseslessons.service.lessonvalidator;

import java.time.LocalDateTime;

public interface LessonCollisionValidatorService {
    void validateIfNewLessonDoesNotCollideWithExistingOnes(LocalDateTime startDateOfLesson, LocalDateTime endDateOfLesson, String tutorEmailAddress, String organizationEmailAddress);
}
