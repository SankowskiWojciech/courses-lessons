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
        List<LessonDates> lessonDatesStub = Lists.newArrayList(
                LessonDatesStub.createWithDates(LocalDateTime.now(), LocalDateTime.now().plusHours(2)),
                LessonDatesStub.createWithDates(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2)),
                LessonDatesStub.createWithDates(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(2))
        );
        List<String> lessonsTitlesStub = Lists.newArrayList(
                LESSON_TITLE_STUB_1,
                LESSON_TITLE_STUB_2
        );
        IndividualLessonsSchedule individualLessonsScheduleStub = IndividualLessonsScheduleStub.createWithLessonsTitles(lessonsTitlesStub);

        //when
        List<IndividualLessonEntity> individualLessonEntities = testee.apply(individualLessonsScheduleStub, lessonDatesStub);

        //then
        assertIndividualLessonEntities(individualLessonEntities, lessonDatesStub, individualLessonsScheduleStub);
    }

    private void assertIndividualLessonEntities(List<IndividualLessonEntity> individualLessonEntitiesResult, List<LessonDates> lessonDatesStub, IndividualLessonsSchedule individualLessonsScheduleStub) {
        assertNotNull(individualLessonEntitiesResult);
        assertEquals(lessonDatesStub.size(), individualLessonEntitiesResult.size());
        List<String> lessonsTitles = individualLessonsScheduleStub.getLessonTitles();
        for (int i = 0; i < lessonDatesStub.size(); i++) {
            IndividualLessonEntity individualLessonEntityResult = individualLessonEntitiesResult.get(i);
            LessonDates expectedLessonDates = lessonDatesStub.get(i);
            String expectedLessonTitle = i < lessonsTitles.size() ? lessonsTitles.get(i) : getDefaultLessonTitle(individualLessonEntityResult.getStudentEntity().getFullName());
            assertEquals(expectedLessonTitle, individualLessonEntityResult.getTitle());
            assertEquals(expectedLessonDates.getStartDate(), individualLessonEntityResult.getStartDateOfLesson());
            assertEquals(expectedLessonDates.getEndDate(), individualLessonEntityResult.getEndDateOfLesson());
            assertEquals(individualLessonsScheduleStub.getOrganizationEntity(), individualLessonEntityResult.getOrganizationEntity());
            assertEquals(individualLessonsScheduleStub.getTutorEntity(), individualLessonEntityResult.getTutorEntity());
            assertEquals(individualLessonsScheduleStub.getStudentEntity(), individualLessonEntityResult.getStudentEntity());
            assertNotNull(individualLessonEntityResult.getCreationDateTime());
        }
    }

    private String getDefaultLessonTitle(String studentFullName) {
        return String.format(DEFAULT_LESSON_TITLE, studentFullName);
    }
}