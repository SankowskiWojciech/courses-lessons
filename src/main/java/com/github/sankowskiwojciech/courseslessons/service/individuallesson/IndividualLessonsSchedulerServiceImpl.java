package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonsScheduleAndListOfLessonDatesToListOfIndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.service.lesson.date.LessonsDatesGeneratorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonCollisionValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
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
    public List<IndividualLessonResponse> scheduleIndividualLessons(IndividualLessonsSchedule schedule) {
        List<LessonDates> lessonDates;
        switch (schedule.getScheduleType()) {
            case ONE_YEAR_LENGTH_LESSONS:
                LocalDate endDate = schedule.getBeginningDate().plusYears(MAX_LESSONS_DURATION_IN_YEARS);
                lessonDates = lessonsDatesGeneratorService.generateLessonsDatesWithFixedBeginningDateAndEndDate(schedule.getBeginningDate(), endDate, schedule.getDaysOfWeekWithTimes());
                return scheduleLessons(schedule, lessonDates);
            case FIXED_DATES_LESSONS:
                lessonDates = lessonsDatesGeneratorService.generateLessonsDatesWithFixedBeginningDateAndEndDate(schedule.getBeginningDate(), schedule.getEndDate(), schedule.getDaysOfWeekWithTimes());
                return scheduleLessons(schedule, lessonDates);
            default:
                lessonDates = lessonsDatesGeneratorService.generateLessonsDatesForFixedDurationLessons(schedule.getBeginningDate(), schedule.getAllLessonsDurationInMinutes(), schedule.getDaysOfWeekWithTimes());
                return scheduleLessons(schedule, lessonDates);
        }
    }

    private List<IndividualLessonResponse> scheduleLessons(IndividualLessonsSchedule schedule, List<LessonDates> generatedDates) {
        lessonCollisionValidatorService.validateIfScheduledLessonsDoesNotCollideWithExistingOnes(generatedDates, schedule.getTutorEntity().getEmailAddress());
        List<IndividualLessonEntity> lessons = IndividualLessonsScheduleAndListOfLessonDatesToListOfIndividualLessonEntity.getInstance().apply(schedule, generatedDates);
        individualLessonRepository.saveAll(lessons);
        return lessons.stream().map(lesson -> IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse.getInstance().apply(lesson, Collections.emptyList())).collect(Collectors.toList());
    }
}
