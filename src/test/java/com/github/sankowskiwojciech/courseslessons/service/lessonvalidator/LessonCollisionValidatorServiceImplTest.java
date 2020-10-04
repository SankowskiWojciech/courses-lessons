package com.github.sankowskiwojciech.courseslessons.service.lessonvalidator;

import com.github.sankowskiwojciech.courseslessons.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.model.exception.NewLessonCollidesWithExistingOnesDetailedException;
import com.github.sankowskiwojciech.courseslessons.model.lesson.LessonDates;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonDatesStub;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.ORGANIZATION_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LessonCollisionValidatorServiceImplTest {

    private final IndividualLessonRepository individualLessonRepositoryMock = mock(IndividualLessonRepository.class);
    private final LessonCollisionValidatorService testee = new LessonCollisionValidatorServiceImpl(individualLessonRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(individualLessonRepositoryMock);
    }

    @Test(expected = NewLessonCollidesWithExistingOnesDetailedException.class)
    public void shouldThrowNewLessonCollidesWithExistingOnesWhenNewLessonCollidesWithExistingOnes() {
        //given
        LocalDateTime startDateOfLessonStub = LocalDateTime.now();
        LocalDateTime endDateOfLessonStub = startDateOfLessonStub.plusHours(2);
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        String organizationEmailAddressStub = ORGANIZATION_EMAIL_ADDRESS_STUB;
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Lists.newArrayList(IndividualLessonEntityStub.createWithDatesOfLesson(startDateOfLessonStub, endDateOfLessonStub));

        when(individualLessonRepositoryMock.findAllLessonsWhichCanCollideWithNewLesson(eq(startDateOfLessonStub), eq(endDateOfLessonStub), eq(tutorEmailAddressStub), eq(organizationEmailAddressStub))).thenReturn(existingIndividualLessonEntitiesStub);

        //when
        try {
            testee.validateIfNewLessonDoesNotCollideWithExistingOnes(startDateOfLessonStub, endDateOfLessonStub, tutorEmailAddressStub, organizationEmailAddressStub);
        } catch (NewLessonCollidesWithExistingOnesDetailedException e) {

            //then exception is thrown
            verify(individualLessonRepositoryMock).findAllLessonsWhichCanCollideWithNewLesson(eq(startDateOfLessonStub), eq(endDateOfLessonStub), eq(tutorEmailAddressStub), eq(organizationEmailAddressStub));
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenNewLessonDoesNotCollideWithExistingOnes() {
        //given
        LocalDateTime startDateOfLessonStub = LocalDateTime.now();
        LocalDateTime endDateOfLessonStub = startDateOfLessonStub.plusHours(2);
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        String organizationEmailAddressStub = null;
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Collections.emptyList();

        when(individualLessonRepositoryMock.findAllLessonsWhichCanCollideWithNewLesson(eq(startDateOfLessonStub), eq(endDateOfLessonStub), eq(tutorEmailAddressStub), isNull())).thenReturn(existingIndividualLessonEntitiesStub);

        //when
        testee.validateIfNewLessonDoesNotCollideWithExistingOnes(startDateOfLessonStub, endDateOfLessonStub, tutorEmailAddressStub, organizationEmailAddressStub);

        //then nothing happens
        verify(individualLessonRepositoryMock).findAllLessonsWhichCanCollideWithNewLesson(eq(startDateOfLessonStub), eq(endDateOfLessonStub), eq(tutorEmailAddressStub), isNull());
    }

    @Test(expected = NewLessonCollidesWithExistingOnesDetailedException.class)
    public void shouldThrowNewLessonCollidesWithExistingOnesExceptionWhenScheduledLessonsCollideWithExistingOnes() {
        //given
        final LocalDateTime currentDateTime = LocalDateTime.now();
        IndividualLessonEntity individualLessonEntityWhichCollidesWithNewOne = IndividualLessonEntityStub.createWithDatesOfLesson(currentDateTime.minusHours(2), currentDateTime.plusHours(2));
        IndividualLessonEntity individualLessonEntityWhichDoesNotCollideWithNewOne = IndividualLessonEntityStub.createWithDatesOfLesson(currentDateTime.plusDays(2), currentDateTime.plusDays(2).plusHours(2));
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Lists.newArrayList(individualLessonEntityWhichCollidesWithNewOne, individualLessonEntityWhichDoesNotCollideWithNewOne);
        LessonDates lessonDatesWhichCollidesWithExistingOne = LessonDatesStub.createWithDates(currentDateTime.minusHours(1), currentDateTime.plusHours(3));
        LessonDates lessonDatesWhichDoesNotCollideWithExistingOne = LessonDatesStub.createWithDates(currentDateTime.plusMonths(1), currentDateTime.plusMonths(1).plusHours(1));
        List<LessonDates> generatedLessonDatesStub = Lists.newArrayList(lessonDatesWhichCollidesWithExistingOne, lessonDatesWhichDoesNotCollideWithExistingOne);
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        String organizationEmailAddressStub = ORGANIZATION_EMAIL_ADDRESS_STUB;

        when(individualLessonRepositoryMock.findAllLessonsInRangeForTutor(isA(LocalDateTime.class), isA(LocalDateTime.class), eq(tutorEmailAddressStub), eq(organizationEmailAddressStub))).thenReturn(existingIndividualLessonEntitiesStub);

        //when
        try {
            testee.validateIfScheduledLessonsDoesNotCollideWithExistingOnes(generatedLessonDatesStub, tutorEmailAddressStub, organizationEmailAddressStub);
        } catch (NewLessonCollidesWithExistingOnesDetailedException e) {

            //then exception is thrown
            verify(individualLessonRepositoryMock).findAllLessonsInRangeForTutor(isA(LocalDateTime.class), isA(LocalDateTime.class), eq(tutorEmailAddressStub), eq(organizationEmailAddressStub));
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenScheduledLessonsDoNotCollideWithExistingOnes() {
        //given
        final LocalDateTime currentDateTime = LocalDateTime.now();
        IndividualLessonEntity individualLessonEntityWhichCollidesWithNewOne = IndividualLessonEntityStub.createWithDatesOfLesson(currentDateTime.minusHours(2), currentDateTime.plusHours(2));
        IndividualLessonEntity individualLessonEntityWhichDoesNotCollideWithNewOne = IndividualLessonEntityStub.createWithDatesOfLesson(currentDateTime.plusDays(2), currentDateTime.plusDays(2).plusHours(2));
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Lists.newArrayList(
                IndividualLessonEntityStub.createWithDatesOfLesson(currentDateTime, currentDateTime.plusHours(2)),
                IndividualLessonEntityStub.createWithDatesOfLesson(currentDateTime.plusHours(2), currentDateTime.plusHours(4))
        );
        LessonDates lessonDatesWhichCollidesWithExistingOne = LessonDatesStub.createWithDates(currentDateTime.minusHours(1), currentDateTime.plusHours(3));
        LessonDates lessonDatesWhichDoesNotCollideWithExistingOne = LessonDatesStub.createWithDates(currentDateTime.plusMonths(1), currentDateTime.plusMonths(1).plusHours(1));
        List<LessonDates> generatedLessonDatesStub = Lists.newArrayList(
                LessonDatesStub.createWithDates(currentDateTime.plusDays(1), currentDateTime.plusDays(1).plusHours(2)),
                LessonDatesStub.createWithDates(currentDateTime.plusDays(1).plusHours(2), currentDateTime.plusDays(1).plusHours(4))
        );
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        String organizationEmailAddressStub = ORGANIZATION_EMAIL_ADDRESS_STUB;

        when(individualLessonRepositoryMock.findAllLessonsInRangeForTutor(isA(LocalDateTime.class), isA(LocalDateTime.class), eq(tutorEmailAddressStub), eq(organizationEmailAddressStub))).thenReturn(existingIndividualLessonEntitiesStub);

        //when
        testee.validateIfScheduledLessonsDoesNotCollideWithExistingOnes(generatedLessonDatesStub, tutorEmailAddressStub, organizationEmailAddressStub);

        //then nothing happens
        verify(individualLessonRepositoryMock).findAllLessonsInRangeForTutor(isA(LocalDateTime.class), isA(LocalDateTime.class), eq(tutorEmailAddressStub), eq(organizationEmailAddressStub));
    }
}