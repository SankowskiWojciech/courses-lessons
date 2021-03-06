package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.NewLessonCollidesWithExistingOnesException;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class LessonCollisionValidatorServiceImpl implements LessonCollisionValidatorService {

    private final IndividualLessonRepository individualLessonRepository;

    @Override
    public void validateIfNewLessonDoesNotCollideWithExistingOnes(LocalDateTime startDateOfLesson, LocalDateTime endDateOfLesson, String tutorEmailAddress, String organizationEmailAddress) {
        List<IndividualLessonEntity> individualLessonEntities = individualLessonRepository.findAllLessonsWhichCanCollideWithNewLesson(startDateOfLesson, endDateOfLesson, tutorEmailAddress, organizationEmailAddress);
        if (!individualLessonEntities.isEmpty()) {
            throw new NewLessonCollidesWithExistingOnesException();
        }
    }

    @Override
    public void validateIfScheduledLessonsDoesNotCollideWithExistingOnes(List<LessonDates> generatedLessonsDates, String tutorEmailAddress, String organizationEmailAddress) {
        LocalDateTime startDateOfLessons = generatedLessonsDates.get(0).getStartDate().toLocalDate().atStartOfDay();
        LocalDateTime endDateOfLessons = generatedLessonsDates.get(generatedLessonsDates.size() - 1).getStartDate().toLocalDate().atStartOfDay().plusDays(1);
        List<IndividualLessonEntity> lessonsWhichCanCollideWithNewLessons = individualLessonRepository.findAllLessonsInRangeForTutor(startDateOfLessons, endDateOfLessons, tutorEmailAddress, organizationEmailAddress);
        generatedLessonsDates.forEach(generatedLessonDates ->
                lessonsWhichCanCollideWithNewLessons.forEach(individualLessonEntity -> {
                    if (generatedLessonDates.getStartDate().isBefore(individualLessonEntity.getEndDateOfLesson()) && generatedLessonDates.getEndDate().isAfter(individualLessonEntity.getStartDateOfLesson())) {
                        throw new NewLessonCollidesWithExistingOnesException();
                    }
                })
        );
    }
}
