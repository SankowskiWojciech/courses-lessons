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
    public List<IndividualLessonEntity> apply(IndividualLessonsSchedule schedule, List<LessonDates> dates) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<IndividualLessonEntity> lessons = new ArrayList<>();
        List<String> titles = schedule.getTitles();
        for (int i = 0; i < dates.size(); i++) {
            IndividualLessonEntity entity = new IndividualLessonEntity();
            entity.setTitle(titles != null && i < titles.size() ? titles.get(i) : getDefaultLessonTitle(schedule.getStudentEntity().getFullName()));
            entity.setStartDate(dates.get(i).getStartDate());
            entity.setEndDate(dates.get(i).getEndDate());
            entity.setOrganizationEntity(schedule.getOrganizationEntity());
            entity.setTutorEntity(schedule.getTutorEntity());
            entity.setStudentEntity(schedule.getStudentEntity());
            entity.setCreationDateTime(currentDateTime);
            lessons.add(entity);
        }
        return lessons;
    }

    private String getDefaultLessonTitle(String studentFullName) {
        return String.format(DEFAULT_LESSON_TITLE, studentFullName);
    }

    public static IndividualLessonsScheduleAndListOfLessonDatesToListOfIndividualLessonEntity getInstance() {
        return INSTANCE;
    }
}
