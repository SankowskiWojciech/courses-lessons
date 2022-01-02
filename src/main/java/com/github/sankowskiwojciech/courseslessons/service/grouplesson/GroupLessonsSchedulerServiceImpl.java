package com.github.sankowskiwojciech.courseslessons.service.grouplesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.GroupLessonEntityAndLessonFilesWithoutContentToGroupLessonResponse;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.GroupLessonsScheduleAndListOfLessonDatesToListOfGroupLessonEntity;
import com.github.sankowskiwojciech.courseslessons.service.lesson.date.LessonsDatesGeneratorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonCollisionValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupLessonsSchedulerServiceImpl implements GroupLessonsSchedulerService {
    private static final long MAX_LESSONS_DURATION_IN_YEARS = 1;

    private final GroupLessonRepository groupLessonRepository;
    private final LessonsDatesGeneratorService lessonsDatesGeneratorService;
    private final LessonCollisionValidatorService lessonCollisionValidatorService;

    @Override
    public List<GroupLessonResponse> scheduleGroupLessons(GroupLessonsSchedule schedule) {
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

    private List<GroupLessonResponse> scheduleLessons(GroupLessonsSchedule schedule, List<LessonDates> generatedDates) {
        lessonCollisionValidatorService.validateIfScheduledLessonsDoesNotCollideWithExistingOnes(generatedDates, schedule.getTutorEntity().getEmailAddress());
        List<GroupLessonEntity> lessons = GroupLessonsScheduleAndListOfLessonDatesToListOfGroupLessonEntity.getInstance().apply(schedule, generatedDates);
        groupLessonRepository.saveAll(lessons);
        return lessons.stream().map(lesson -> GroupLessonEntityAndLessonFilesWithoutContentToGroupLessonResponse.getInstance().apply(lesson, Collections.emptyList())).collect(Collectors.toList());
    }
}
