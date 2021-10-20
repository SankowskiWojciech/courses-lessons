package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
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
    private final GroupLessonRepository groupLessonRepository;

    @Override
    public void validateIfNewLessonDoesNotCollideWithExistingOnes(LocalDateTime startDate, LocalDateTime endDate, String tutorEmailAddress) {
        List<IndividualLessonEntity> individualLessons = individualLessonRepository.findAllLessonsWhichCanCollideWithNewLesson(startDate, endDate, tutorEmailAddress);
        if (!individualLessons.isEmpty()) {
            throw new NewLessonCollidesWithExistingOnesException();
        }
        List<GroupLessonEntity> groupLessons = groupLessonRepository.findAllLessonsWhichCanCollideWithNewLesson(startDate, endDate, tutorEmailAddress);
        if (!groupLessons.isEmpty()) {
            throw new NewLessonCollidesWithExistingOnesException();
        }
    }

    @Override
    public void validateIfScheduledLessonsDoesNotCollideWithExistingOnes(List<LessonDates> generatedDates, String tutorEmailAddress) {
        LocalDateTime startDate = generatedDates.get(0).getStartDate().toLocalDate().atStartOfDay();
        LocalDateTime endDate = generatedDates.get(generatedDates.size() - 1).getStartDate().toLocalDate().atStartOfDay().plusDays(1);
        List<IndividualLessonEntity> lessonsWhichCanCollideWithNewLessons = individualLessonRepository.findAllLessonsInRangeForTutor(startDate, endDate, tutorEmailAddress);
        generatedDates.forEach(dates ->
                lessonsWhichCanCollideWithNewLessons.forEach(lesson -> {
                    if (dates.getStartDate().isBefore(lesson.getEndDate()) && dates.getEndDate().isAfter(lesson.getStartDate())) {
                        throw new NewLessonCollidesWithExistingOnesException();
                    }
                })
        );
    }
}
