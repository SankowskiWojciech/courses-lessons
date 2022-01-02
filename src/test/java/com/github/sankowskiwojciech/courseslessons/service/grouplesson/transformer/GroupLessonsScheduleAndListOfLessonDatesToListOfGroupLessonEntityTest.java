package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupLessonsScheduleStub;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonDatesStub;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GroupLessonsScheduleAndListOfLessonDatesToListOfGroupLessonEntityTest {
    private static final String LESSON_TITLE_STUB_1 = "LESSON_TITLE_STUB_1";
    private static final String LESSON_TITLE_STUB_2 = "LESSON_TITLE_STUB_2";
    private static final String DEFAULT_LESSON_TITLE = "Zajęcia: %s";

    private final GroupLessonsScheduleAndListOfLessonDatesToListOfGroupLessonEntity testee = GroupLessonsScheduleAndListOfLessonDatesToListOfGroupLessonEntity.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        List<LessonDates> datesStub = Lists.newArrayList(
                LessonDatesStub.createWithDates(LocalDateTime.now(), LocalDateTime.now().plusHours(2)),
                LessonDatesStub.createWithDates(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2)),
                LessonDatesStub.createWithDates(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(2))
        );
        List<String> titlesStub = Lists.newArrayList(
                LESSON_TITLE_STUB_1,
                LESSON_TITLE_STUB_2
        );
        GroupLessonsSchedule scheduleStub = GroupLessonsScheduleStub.createWithLessonsTitles(titlesStub);

        //when
        List<GroupLessonEntity> entities = testee.apply(scheduleStub, datesStub);

        //then
        assertGroupLessonEntities(entities, datesStub, scheduleStub);
    }

    private void assertGroupLessonEntities(List<GroupLessonEntity> entities, List<LessonDates> datesStub, GroupLessonsSchedule scheduleStub) {
        assertNotNull(entities);
        assertEquals(datesStub.size(), entities.size());
        List<String> titles = scheduleStub.getTitles();
        for (int i = 0; i < datesStub.size(); i++) {
            GroupLessonEntity entity = entities.get(i);
            LessonDates expectedLessonDates = datesStub.get(i);
            String expectedLessonTitle = i < titles.size() ? titles.get(i) : getDefaultLessonTitle(entity.getGroupEntity().getName());
            assertEquals(expectedLessonTitle, entity.getTitle());
            assertEquals(expectedLessonDates.getStartDate(), entity.getStartDate());
            assertEquals(expectedLessonDates.getEndDate(), entity.getEndDate());
            assertEquals(scheduleStub.getSubdomainEntity(), entity.getSubdomainEntity());
            assertEquals(scheduleStub.getTutorEntity(), entity.getTutorEntity());
            assertEquals(scheduleStub.getGroupEntity(), entity.getGroupEntity());
            assertNotNull(entity.getCreationDateTime());
        }
    }

    private String getDefaultLessonTitle(String groupName) {
        return String.format(DEFAULT_LESSON_TITLE, groupName);
    }
}