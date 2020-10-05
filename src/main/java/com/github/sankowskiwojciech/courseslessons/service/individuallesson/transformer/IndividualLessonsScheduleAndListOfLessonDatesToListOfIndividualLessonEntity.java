package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonsScheduleAndListOfLessonDatesToListOfIndividualLessonEntity implements BiFunction<IndividualLessonsSchedule, List<LessonDates>, List<IndividualLessonEntity>> {

    //TODO: move it to external property file
    private static final String DEFAULT_LESSON_TITLE = "Zajęcia: %s";

    private static final IndividualLessonsScheduleAndListOfLessonDatesToListOfIndividualLessonEntity INSTANCE = new IndividualLessonsScheduleAndListOfLessonDatesToListOfIndividualLessonEntity();

    @Override
    public List<IndividualLessonEntity> apply(IndividualLessonsSchedule individualLessonsSchedule, List<LessonDates> lessonsDates) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<IndividualLessonEntity> individualLessonEntities = new ArrayList<>();
        List<String> lessonsTitles = individualLessonsSchedule.getLessonsTitles();
        for (int i = 0; i < lessonsDates.size(); i++) {
            IndividualLessonEntity individualLessonEntity = IndividualLessonEntity.builder()
                    .title(lessonsTitles != null && i < lessonsTitles.size() ? lessonsTitles.get(i) : getDefaultLessonTitle(individualLessonsSchedule.getStudentEntity().getFullName()))
                    .startDateOfLesson(lessonsDates.get(i).getStartDate())
                    .endDateOfLesson(lessonsDates.get(i).getEndDate())
                    .organizationEntity(individualLessonsSchedule.getOrganizationEntity())
                    .tutorEntity(individualLessonsSchedule.getTutorEntity())
                    .studentEntity(individualLessonsSchedule.getStudentEntity())
                    .creationDateTime(currentDateTime)
                    .build();
            individualLessonEntities.add(individualLessonEntity);
        }
        return individualLessonEntities;
    }

    private String getDefaultLessonTitle(String studentFullName) {
        return String.format(DEFAULT_LESSON_TITLE, studentFullName);
    }

    public static IndividualLessonsScheduleAndListOfLessonDatesToListOfIndividualLessonEntity getInstance() {
        return INSTANCE;
    }
}
