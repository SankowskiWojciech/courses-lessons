package com.github.sankowskiwojciech.courseslessons.service.grouplesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.ScheduleType;
import com.github.sankowskiwojciech.courseslessons.service.lesson.date.LessonsDatesGeneratorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonCollisionValidatorService;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupLessonsScheduleStub;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonDatesStub;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupLessonsSchedulerServiceImplTest {

    private final GroupLessonRepository groupLessonRepositoryMock = mock(GroupLessonRepository.class);
    private final LessonsDatesGeneratorService lessonsDatesGeneratorServiceMock = mock(LessonsDatesGeneratorService.class);
    private final LessonCollisionValidatorService lessonCollisionValidatorServiceMock = mock(LessonCollisionValidatorService.class);
    private final GroupLessonsSchedulerService testee = new GroupLessonsSchedulerServiceImpl(groupLessonRepositoryMock, lessonsDatesGeneratorServiceMock, lessonCollisionValidatorServiceMock);

    @Before
    public void reset() {
        Mockito.reset(groupLessonRepositoryMock, lessonsDatesGeneratorServiceMock, lessonCollisionValidatorServiceMock);
    }

    @Test
    public void shouldScheduleGroupLessonsWhenScheduleTypeIsOneYearLengthLessons() {
        //given
        GroupLessonsSchedule scheduleStub = GroupLessonsScheduleStub.createWithScheduleType(ScheduleType.ONE_YEAR_LENGTH_LESSONS);
        List<LessonDates> datesStub = Lists.newArrayList(
                LessonDatesStub.createWithDates(LocalDateTime.now(), LocalDateTime.now().plusHours(3)),
                LessonDatesStub.createWithDates(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(3))
        );

        when(lessonsDatesGeneratorServiceMock.generateLessonsDatesWithFixedBeginningDateAndEndDate(eq(scheduleStub.getBeginningDate()), isA(LocalDate.class), eq(scheduleStub.getDaysOfWeekWithTimes()))).thenReturn(datesStub);

        //when
        List<GroupLessonResponse> responses = testee.scheduleGroupLessons(scheduleStub);

        //then
        verify(lessonsDatesGeneratorServiceMock).generateLessonsDatesWithFixedBeginningDateAndEndDate(eq(scheduleStub.getBeginningDate()), isA(LocalDate.class), eq(scheduleStub.getDaysOfWeekWithTimes()));
        verify(lessonCollisionValidatorServiceMock).validateIfScheduledLessonsDoesNotCollideWithExistingOnes(eq(datesStub), eq(scheduleStub.getTutorEntity().getEmailAddress()));
        verify(groupLessonRepositoryMock).saveAll(anyList());

        assertNotNull(responses);
        assertEquals(datesStub.size(), responses.size());
    }

    @Test
    public void shouldScheduleGroupLessonsWhenScheduleTypeIsFixedDatesLessons() {
        //given
        GroupLessonsSchedule scheduleStub = GroupLessonsScheduleStub.createWithScheduleType(ScheduleType.FIXED_DATES_LESSONS);
        List<LessonDates> datesStub = Lists.newArrayList(
                LessonDatesStub.createWithDates(LocalDateTime.now(), LocalDateTime.now().plusHours(3)),
                LessonDatesStub.createWithDates(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(3))
        );

        when(lessonsDatesGeneratorServiceMock.generateLessonsDatesWithFixedBeginningDateAndEndDate(eq(scheduleStub.getBeginningDate()), eq(scheduleStub.getEndDate()), eq(scheduleStub.getDaysOfWeekWithTimes()))).thenReturn(datesStub);

        //when
        List<GroupLessonResponse> responses = testee.scheduleGroupLessons(scheduleStub);

        //then
        verify(lessonsDatesGeneratorServiceMock).generateLessonsDatesWithFixedBeginningDateAndEndDate(eq(scheduleStub.getBeginningDate()), eq(scheduleStub.getEndDate()), eq(scheduleStub.getDaysOfWeekWithTimes()));
        verify(lessonCollisionValidatorServiceMock).validateIfScheduledLessonsDoesNotCollideWithExistingOnes(eq(datesStub), eq(scheduleStub.getTutorEntity().getEmailAddress()));
        verify(groupLessonRepositoryMock).saveAll(anyList());

        assertNotNull(responses);
        assertEquals(datesStub.size(), responses.size());
    }

    @Test
    public void shouldScheduleGroupLessonsWhenScheduleTypeIsFixedDurationLessons() {
        //given
        GroupLessonsSchedule scheduleStub = GroupLessonsScheduleStub.createWithScheduleType(ScheduleType.FIXED_DURATION_LESSONS);
        List<LessonDates> datesStub = Lists.newArrayList(
                LessonDatesStub.createWithDates(LocalDateTime.now(), LocalDateTime.now().plusHours(2)),
                LessonDatesStub.createWithDates(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2))
        );

        when(lessonsDatesGeneratorServiceMock.generateLessonsDatesForFixedDurationLessons(eq(scheduleStub.getBeginningDate()), eq(scheduleStub.getAllLessonsDurationInMinutes()), eq(scheduleStub.getDaysOfWeekWithTimes()))).thenReturn(datesStub);

        //when
        List<GroupLessonResponse> responses = testee.scheduleGroupLessons(scheduleStub);

        //then
        verify(lessonsDatesGeneratorServiceMock).generateLessonsDatesForFixedDurationLessons(eq(scheduleStub.getBeginningDate()), eq(scheduleStub.getAllLessonsDurationInMinutes()), eq(scheduleStub.getDaysOfWeekWithTimes()));
        verify(lessonCollisionValidatorServiceMock).validateIfScheduledLessonsDoesNotCollideWithExistingOnes(eq(datesStub), eq(scheduleStub.getTutorEntity().getEmailAddress()));
        verify(groupLessonRepositoryMock).saveAll(anyList());

        assertNotNull(responses);
        assertEquals(datesStub.size(), responses.size());
    }
}