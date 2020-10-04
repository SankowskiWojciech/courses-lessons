package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.courseslessons.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.courseslessons.model.lesson.LessonDates;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonEntityToIndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonsScheduleAndListOfLessonDatesToListOfIndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.service.lesson.LessonsDatesGeneratorService;
import com.github.sankowskiwojciech.courseslessons.service.lessonvalidator.LessonCollisionValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IndividualLessonsSchedulerServiceImpl implements IndividualLessonsSchedulerService {

    private static final long MAX_LESSONS_DURATION_IN_YEARS = 1;

    private final IndividualLessonRepository individualLessonRepository;
    private final LessonsDatesGeneratorService lessonsDatesGeneratorService;
    private final LessonCollisionValidatorService lessonCollisionValidatorService;

    @Transactional
    @Override
    public List<IndividualLessonResponse> scheduleIndividualLessons(IndividualLessonsSchedule individualLessonsSchedule) {
        List<LessonDates> generatedLessonsDates;
        switch (individualLessonsSchedule.getScheduleType()) {
            case ONE_YEAR_LENGTH_LESSONS:
                LocalDate lessonsEndDate = individualLessonsSchedule.getBeginningDate().plusYears(MAX_LESSONS_DURATION_IN_YEARS);
                generatedLessonsDates = lessonsDatesGeneratorService.generateLessonsDatesWithFixedBeginningDateAndEndDate(individualLessonsSchedule.getBeginningDate(), lessonsEndDate, individualLessonsSchedule.getLessonsDaysOfWeekWithTimes());
                return scheduleLessons(individualLessonsSchedule, generatedLessonsDates);
            case FIXED_DATES_LESSONS:
                generatedLessonsDates = lessonsDatesGeneratorService.generateLessonsDatesWithFixedBeginningDateAndEndDate(individualLessonsSchedule.getBeginningDate(), individualLessonsSchedule.getEndDate(), individualLessonsSchedule.getLessonsDaysOfWeekWithTimes());
                return scheduleLessons(individualLessonsSchedule, generatedLessonsDates);
            default:
                generatedLessonsDates = lessonsDatesGeneratorService.generateLessonsDatesForFixedDurationLessons(individualLessonsSchedule.getBeginningDate(), individualLessonsSchedule.getAllLessonsDurationInMinutes(), individualLessonsSchedule.getLessonsDaysOfWeekWithTimes());
                return scheduleLessons(individualLessonsSchedule, generatedLessonsDates);
        }
    }

    private List<IndividualLessonResponse> scheduleLessons(IndividualLessonsSchedule individualLessonsSchedule, List<LessonDates> generatedLessonsDates) {
        String organizationEmailAddress = individualLessonsSchedule.getOrganizationEntity() != null ? individualLessonsSchedule.getOrganizationEntity().getEmailAddress() : null;
        lessonCollisionValidatorService.validateIfScheduledLessonsDoesNotCollideWithExistingOnes(generatedLessonsDates, individualLessonsSchedule.getTutorEntity().getEmailAddress(), organizationEmailAddress);
        List<IndividualLessonEntity> individualLessonEntities = IndividualLessonsScheduleAndListOfLessonDatesToListOfIndividualLessonEntity.getInstance().apply(individualLessonsSchedule, generatedLessonsDates);
        individualLessonRepository.saveAll(individualLessonEntities);
        return individualLessonEntities.stream().map(individualLessonEntity -> IndividualLessonEntityToIndividualLessonResponse.getInstance().apply(individualLessonEntity)).collect(Collectors.toList());
    }
}