package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.courseslessons.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.courseslessons.model.lesson.LessonDates;
import com.github.sankowskiwojciech.courseslessons.model.lesson.ScheduleType;
import com.github.sankowskiwojciech.courseslessons.service.lesson.DatesGeneratorService;
import com.github.sankowskiwojciech.courseslessons.service.lessonvalidator.LessonCollisionValidatorService;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonsScheduleStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonDatesStub;
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

public class IndividualLessonsSchedulerServiceImplTest {

    private final IndividualLessonRepository individualLessonRepositoryMock = mock(IndividualLessonRepository.class);
    private final DatesGeneratorService datesGeneratorServiceMock = mock(DatesGeneratorService.class);
    private final LessonCollisionValidatorService lessonCollisionValidatorServiceMock = mock(LessonCollisionValidatorService.class);
    private final IndividualLessonsSchedulerService testee = new IndividualLessonsSchedulerServiceImpl(individualLessonRepositoryMock, datesGeneratorServiceMock, lessonCollisionValidatorServiceMock);

    @Before
    public void reset() {
        Mockito.reset(individualLessonRepositoryMock, datesGeneratorServiceMock, lessonCollisionValidatorServiceMock);
    }

    @Test
    public void shouldScheduleIndividualLessonsWhenScheduleTypeIsOneYearLengthLessons() {
        //given
        IndividualLessonsSchedule individualLessonsScheduleStub = IndividualLessonsScheduleStub.createWithScheduleType(ScheduleType.ONE_YEAR_LENGTH_LESSONS);
        List<LessonDates> lessonsDatesStub = Lists.newArrayList(
                LessonDatesStub.createWithDates(LocalDateTime.now(), LocalDateTime.now().plusHours(3)),
                LessonDatesStub.createWithDates(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(3))
        );

        when(datesGeneratorServiceMock.generateLessonsDatesWithFixedBeginningDateAndEndDate(eq(individualLessonsScheduleStub.getBeginningDate()), isA(LocalDate.class), eq(individualLessonsScheduleStub.getLessonsDaysOfWeekWithTimes()))).thenReturn(lessonsDatesStub);

        //when
        List<IndividualLessonResponse> individualLessonResponses = testee.scheduleIndividualLessons(individualLessonsScheduleStub);

        //then
        verify(datesGeneratorServiceMock).generateLessonsDatesWithFixedBeginningDateAndEndDate(eq(individualLessonsScheduleStub.getBeginningDate()), isA(LocalDate.class), eq(individualLessonsScheduleStub.getLessonsDaysOfWeekWithTimes()));
        verify(lessonCollisionValidatorServiceMock).validateIfScheduledLessonsDoesNotCollideWithExistingOnes(eq(lessonsDatesStub), eq(individualLessonsScheduleStub.getTutorEntity().getEmailAddress()), eq(individualLessonsScheduleStub.getOrganizationEntity().getEmailAddress()));
        verify(individualLessonRepositoryMock).saveAll(anyList());

        assertNotNull(individualLessonResponses);
        assertEquals(lessonsDatesStub.size(), individualLessonResponses.size());
    }

    @Test
    public void shouldScheduleIndividualLessonsWhenScheduleTypeIsFixedDatesLessons() {
        //given
        IndividualLessonsSchedule individualLessonsScheduleStub = IndividualLessonsScheduleStub.createWithScheduleType(ScheduleType.FIXED_DATES_LESSONS);
        List<LessonDates> lessonsDatesStub = Lists.newArrayList(
                LessonDatesStub.createWithDates(LocalDateTime.now(), LocalDateTime.now().plusHours(3)),
                LessonDatesStub.createWithDates(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(3))
        );

        when(datesGeneratorServiceMock.generateLessonsDatesWithFixedBeginningDateAndEndDate(eq(individualLessonsScheduleStub.getBeginningDate()), eq(individualLessonsScheduleStub.getEndDate()), eq(individualLessonsScheduleStub.getLessonsDaysOfWeekWithTimes()))).thenReturn(lessonsDatesStub);

        //when
        List<IndividualLessonResponse> individualLessonResponses = testee.scheduleIndividualLessons(individualLessonsScheduleStub);

        //then
        verify(datesGeneratorServiceMock).generateLessonsDatesWithFixedBeginningDateAndEndDate(eq(individualLessonsScheduleStub.getBeginningDate()), eq(individualLessonsScheduleStub.getEndDate()), eq(individualLessonsScheduleStub.getLessonsDaysOfWeekWithTimes()));
        verify(lessonCollisionValidatorServiceMock).validateIfScheduledLessonsDoesNotCollideWithExistingOnes(eq(lessonsDatesStub), eq(individualLessonsScheduleStub.getTutorEntity().getEmailAddress()), eq(individualLessonsScheduleStub.getOrganizationEntity().getEmailAddress()));
        verify(individualLessonRepositoryMock).saveAll(anyList());

        assertNotNull(individualLessonResponses);
        assertEquals(lessonsDatesStub.size(), individualLessonResponses.size());
    }
}