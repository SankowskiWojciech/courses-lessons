package com.github.sankowskiwojciech.courseslessons.service.lessonvalidator;

import com.github.sankowskiwojciech.courseslessons.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.model.exception.NewLessonCollidesWithExistingOnes;
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
        List<IndividualLessonEntity> individualLessonEntities;
        if (organizationEmailAddress == null) {
            individualLessonEntities = individualLessonRepository.findAllIndividualLessonsWhichCanCollideWithNewIndividualLessonForTutorAsSubdomain(startDateOfLesson, endDateOfLesson, tutorEmailAddress);
        } else {
            individualLessonEntities = individualLessonRepository.findAllIndividualLessonsWhichCanCollideWithNewIndividualLessonForOrganizationAsSubdomain(startDateOfLesson, endDateOfLesson, tutorEmailAddress, organizationEmailAddress);
        }
        if (!individualLessonEntities.isEmpty()) {
            throw new NewLessonCollidesWithExistingOnes();
        }
    }
}
