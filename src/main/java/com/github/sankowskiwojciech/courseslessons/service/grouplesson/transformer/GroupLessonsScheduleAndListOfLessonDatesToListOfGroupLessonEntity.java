package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupLessonsScheduleAndListOfLessonDatesToListOfGroupLessonEntity implements BiFunction<GroupLessonsSchedule, List<LessonDates>, List<GroupLessonEntity>> {

    //TODO: move it to external property file
    private static final String DEFAULT_LESSON_TITLE = "Zajęcia: %s";

    private static final GroupLessonsScheduleAndListOfLessonDatesToListOfGroupLessonEntity INSTANCE = new GroupLessonsScheduleAndListOfLessonDatesToListOfGroupLessonEntity();

    @Override
    public List<GroupLessonEntity> apply(GroupLessonsSchedule schedule, List<LessonDates> dates) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<GroupLessonEntity> lessons = new ArrayList<>();
        List<String> titles = schedule.getTitles();
        for (int i = 0; i < dates.size(); i++) {
            GroupLessonEntity entity = new GroupLessonEntity();
            entity.setTitle(titles != null && i < titles.size() ? titles.get(i) : getDefaultLessonTitle(schedule.getGroupEntity().getName()));
            entity.setStartDate(dates.get(i).getStartDate());
            entity.setEndDate(dates.get(i).getEndDate());
            entity.setSubdomainEntity(schedule.getSubdomainEntity());
            entity.setTutorEntity(schedule.getTutorEntity());
            entity.setGroupEntity(schedule.getGroupEntity());
            entity.setCreationDateTime(currentDateTime);
            lessons.add(entity);
        }
        return lessons;
    }

    private String getDefaultLessonTitle(String groupName) {
        return String.format(DEFAULT_LESSON_TITLE, groupName);
    }

    public static GroupLessonsScheduleAndListOfLessonDatesToListOfGroupLessonEntity getInstance() {
        return INSTANCE;
    }
}
