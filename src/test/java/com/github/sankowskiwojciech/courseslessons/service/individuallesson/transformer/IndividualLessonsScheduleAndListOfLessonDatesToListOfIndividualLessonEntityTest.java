package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonsScheduleStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonDatesStub;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IndividualLessonsScheduleAndListOfLessonDatesToListOfIndividualLessonEntityTest {
    private static final String LESSON_TITLE_STUB_1 = "LESSON_TITLE_STUB_1";
    private static final String LESSON_TITLE_STUB_2 = "LESSON_TITLE_STUB_2";
    private static final String DEFAULT_LESSON_TITLE = "Zajęcia: %s";

    private final IndividualLessonsScheduleAndListOfLessonDatesToListOfIndividualLessonEntity testee = IndividualLessonsScheduleAndListOfLessonDatesToListOfIndividualLessonEntity.getInstance();

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
        IndividualLessonsSchedule scheduleStub = IndividualLessonsScheduleStub.createWithLessonsTitles(titlesStub);

        //when
        List<IndividualLessonEntity> entities = testee.apply(scheduleStub, datesStub);

        //then
        assertIndividualLessonEntities(entities, datesStub, scheduleStub);
    }

    private void assertIndividualLessonEntities(List<IndividualLessonEntity> entities, List<LessonDates> datesStub, IndividualLessonsSchedule scheduleStub) {
        assertNotNull(entities);
        assertEquals(datesStub.size(), entities.size());
        List<String> titles = scheduleStub.getTitles();
        for (int i = 0; i < datesStub.size(); i++) {
            IndividualLessonEntity entity = entities.get(i);
            LessonDates expectedLessonDates = datesStub.get(i);
            String expectedLessonTitle = i < titles.size() ? titles.get(i) : getDefaultLessonTitle(entity.getStudentEntity().getFullName());
            assertEquals(expectedLessonTitle, entity.getTitle());
            assertEquals(expectedLessonDates.getStartDate(), entity.getStartDate());
            assertEquals(expectedLessonDates.getEndDate(), entity.getEndDate());
            assertEquals(scheduleStub.getSubdomainEntity(), entity.getSubdomainEntity());
            assertEquals(scheduleStub.getTutorEntity(), entity.getTutorEntity());
            assertEquals(scheduleStub.getStudentEntity(), entity.getStudentEntity());
            assertNotNull(entity.getCreationDateTime());
        }
    }

    private String getDefaultLessonTitle(String studentFullName) {
        return String.format(DEFAULT_LESSON_TITLE, studentFullName);
    }
}