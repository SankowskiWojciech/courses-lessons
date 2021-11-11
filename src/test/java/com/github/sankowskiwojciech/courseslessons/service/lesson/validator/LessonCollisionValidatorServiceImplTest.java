package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.NewLessonCollidesWithExistingOnesException;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonDates;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupLessonEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonDatesStub;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class LessonCollisionValidatorServiceImplTest {

    private final IndividualLessonRepository individualLessonRepositoryMock = mock(IndividualLessonRepository.class);
    private final GroupLessonRepository groupLessonRepositoryMock = mock(GroupLessonRepository.class);
    private final LessonCollisionValidatorService testee = new LessonCollisionValidatorServiceImpl(individualLessonRepositoryMock, groupLessonRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(individualLessonRepositoryMock, groupLessonRepositoryMock);
    }

    @Test(expected = NewLessonCollidesWithExistingOnesException.class)
    public void shouldThrowNewLessonCollidesWithExistingOnesWhenNewLessonCollidesWithIndividualLessons() {
        //given
        LocalDateTime startDateStub = LocalDateTime.now();
        LocalDateTime endDateStub = startDateStub.plusHours(2);
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Lists.newArrayList(IndividualLessonEntityStub.createWithDatesOfLesson(startDateStub, endDateStub));

        when(individualLessonRepositoryMock.findAllLessonsWhichCanCollideWithNewLesson(eq(startDateStub), eq(endDateStub), eq(tutorEmailAddressStub))).thenReturn(existingIndividualLessonEntitiesStub);

        //when
        try {
            testee.validateIfNewLessonDoesNotCollideWithExistingOnes(startDateStub, endDateStub, tutorEmailAddressStub);
        } catch (NewLessonCollidesWithExistingOnesException e) {

            //then exception is thrown
            verify(individualLessonRepositoryMock).findAllLessonsWhichCanCollideWithNewLesson(eq(startDateStub), eq(endDateStub), eq(tutorEmailAddressStub));
            verifyNoInteractions(groupLessonRepositoryMock);
            throw e;
        }
    }

    @Test(expected = NewLessonCollidesWithExistingOnesException.class)
    public void shouldThrowNewLessonCollidesWithExistingOnesWhenNewLessonCollidesWithGroupLessons() {
        //given
        LocalDateTime startDateStub = LocalDateTime.now();
        LocalDateTime endDateStub = startDateStub.plusHours(2);
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Collections.emptyList();
        List<GroupLessonEntity> existingGroupLessonEntitiesStub = Lists.newArrayList(GroupLessonEntityStub.createWithDatesOfLesson(startDateStub, endDateStub));

        when(individualLessonRepositoryMock.findAllLessonsWhichCanCollideWithNewLesson(eq(startDateStub), eq(endDateStub), eq(tutorEmailAddressStub))).thenReturn(existingIndividualLessonEntitiesStub);
        when(groupLessonRepositoryMock.findAllLessonsWhichCanCollideWithNewLesson(startDateStub, endDateStub, tutorEmailAddressStub)).thenReturn(existingGroupLessonEntitiesStub);

        //when
        try {
            testee.validateIfNewLessonDoesNotCollideWithExistingOnes(startDateStub, endDateStub, tutorEmailAddressStub);
        } catch (NewLessonCollidesWithExistingOnesException e) {

            //then exception is thrown
            verify(individualLessonRepositoryMock).findAllLessonsWhichCanCollideWithNewLesson(eq(startDateStub), eq(endDateStub), eq(tutorEmailAddressStub));
            verify(groupLessonRepositoryMock).findAllLessonsWhichCanCollideWithNewLesson(eq(startDateStub), eq(endDateStub), eq(tutorEmailAddressStub));
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenNewLessonDoesNotCollideWithExistingOnes() {
        //given
        LocalDateTime startDateStub = LocalDateTime.now();
        LocalDateTime endDateStub = startDateStub.plusHours(2);
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Collections.emptyList();
        List<GroupLessonEntity> existingGroupLessonEntitiesStub = Collections.emptyList();

        when(individualLessonRepositoryMock.findAllLessonsWhichCanCollideWithNewLesson(eq(startDateStub), eq(endDateStub), eq(tutorEmailAddressStub))).thenReturn(existingIndividualLessonEntitiesStub);
        when(groupLessonRepositoryMock.findAllLessonsWhichCanCollideWithNewLesson(startDateStub, endDateStub, tutorEmailAddressStub)).thenReturn(existingGroupLessonEntitiesStub);

        //when
        testee.validateIfNewLessonDoesNotCollideWithExistingOnes(startDateStub, endDateStub, tutorEmailAddressStub);

        //then nothing happens
        verify(individualLessonRepositoryMock).findAllLessonsWhichCanCollideWithNewLesson(eq(startDateStub), eq(endDateStub), eq(tutorEmailAddressStub));
        verify(groupLessonRepositoryMock).findAllLessonsWhichCanCollideWithNewLesson(eq(startDateStub), eq(endDateStub), eq(tutorEmailAddressStub));
    }

    @Test(expected = NewLessonCollidesWithExistingOnesException.class)
    public void shouldThrowNewLessonCollidesWithExistingOnesExceptionWhenScheduledLessonsCollideWithExistingOnes() {
        //given
        final LocalDateTime currentDateTime = LocalDateTime.now();
        IndividualLessonEntity individualLessonEntityWhichCollidesWithNewOne = IndividualLessonEntityStub.createWithDatesOfLesson(currentDateTime.minusHours(2), currentDateTime.plusHours(2));
        IndividualLessonEntity individualLessonEntityWhichDoesNotCollideWithNewOne = IndividualLessonEntityStub.createWithDatesOfLesson(currentDateTime.plusDays(2), currentDateTime.plusDays(2).plusHours(2));
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Lists.newArrayList(individualLessonEntityWhichCollidesWithNewOne, individualLessonEntityWhichDoesNotCollideWithNewOne);
        LessonDates lessonDatesWhichCollidesWithExistingOne = LessonDatesStub.createWithDates(currentDateTime.minusHours(1), currentDateTime.plusHours(3));
        LessonDates lessonDatesWhichDoesNotCollideWithExistingOne = LessonDatesStub.createWithDates(currentDateTime.plusMonths(1), currentDateTime.plusMonths(1).plusHours(1));
        List<LessonDates> generatedDatesStub = Lists.newArrayList(lessonDatesWhichCollidesWithExistingOne, lessonDatesWhichDoesNotCollideWithExistingOne);
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;

        when(individualLessonRepositoryMock.findAllLessonsInRangeForTutor(isA(LocalDateTime.class), isA(LocalDateTime.class), eq(tutorEmailAddressStub))).thenReturn(existingIndividualLessonEntitiesStub);

        //when
        try {
            testee.validateIfScheduledLessonsDoesNotCollideWithExistingOnes(generatedDatesStub, tutorEmailAddressStub);
        } catch (NewLessonCollidesWithExistingOnesException e) {

            //then exception is thrown
            verify(individualLessonRepositoryMock).findAllLessonsInRangeForTutor(isA(LocalDateTime.class), isA(LocalDateTime.class), eq(tutorEmailAddressStub));
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenScheduledLessonsDoNotCollideWithExistingOnes() {
        //given
        final LocalDateTime currentDateTime = LocalDateTime.now();
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Lists.newArrayList(
                IndividualLessonEntityStub.createWithDatesOfLesson(currentDateTime, currentDateTime.plusHours(2)),
                IndividualLessonEntityStub.createWithDatesOfLesson(currentDateTime.plusHours(2), currentDateTime.plusHours(4))
        );
        List<LessonDates> generatedDatesStub = Lists.newArrayList(
                LessonDatesStub.createWithDates(currentDateTime.plusDays(1), currentDateTime.plusDays(1).plusHours(2)),
                LessonDatesStub.createWithDates(currentDateTime.plusDays(1).plusHours(2), currentDateTime.plusDays(1).plusHours(4))
        );
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;

        when(individualLessonRepositoryMock.findAllLessonsInRangeForTutor(isA(LocalDateTime.class), isA(LocalDateTime.class), eq(tutorEmailAddressStub))).thenReturn(existingIndividualLessonEntitiesStub);

        //when
        testee.validateIfScheduledLessonsDoesNotCollideWithExistingOnes(generatedDatesStub, tutorEmailAddressStub);

        //then nothing happens
        verify(individualLessonRepositoryMock).findAllLessonsInRangeForTutor(isA(LocalDateTime.class), isA(LocalDateTime.class), eq(tutorEmailAddressStub));
    }
}