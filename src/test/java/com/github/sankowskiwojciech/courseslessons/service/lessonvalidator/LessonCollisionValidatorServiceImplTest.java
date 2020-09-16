package com.github.sankowskiwojciech.courseslessons.service.lessonvalidator;

import com.github.sankowskiwojciech.courseslessons.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.model.exception.NewLessonCollidesWithExistingOnes;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
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

    @Test(expected = NewLessonCollidesWithExistingOnes.class)
    public void shouldThrowNewLessonCollidesWithExistingOnesWhenNewLessonCollidesWithExistingOnesAndSubdomainIsTutor() {
        //given
        LocalDateTime startDateOfLessonStub = LocalDateTime.now();
        LocalDateTime endDateOfLessonStub = startDateOfLessonStub.plusHours(2);
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        String organizationEmailAddressStub = null;
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Lists.newArrayList(IndividualLessonEntityStub.createWithDatesOfLesson(startDateOfLessonStub, endDateOfLessonStub));

        when(individualLessonRepositoryMock.findAllIndividualLessonsWhichCanCollideWithNewIndividualLessonForTutorAsSubdomain(eq(startDateOfLessonStub), eq(endDateOfLessonStub), eq(tutorEmailAddressStub))).thenReturn(existingIndividualLessonEntitiesStub);

        //when
        try {
            testee.validateIfNewLessonDoesNotCollideWithExistingOnes(startDateOfLessonStub, endDateOfLessonStub, tutorEmailAddressStub, organizationEmailAddressStub);
        } catch (NewLessonCollidesWithExistingOnes e) {

            //then exception is thrown
            verify(individualLessonRepositoryMock).findAllIndividualLessonsWhichCanCollideWithNewIndividualLessonForTutorAsSubdomain(eq(startDateOfLessonStub), eq(endDateOfLessonStub), eq(tutorEmailAddressStub));
            throw e;
        }
    }

    @Test(expected = NewLessonCollidesWithExistingOnes.class)
    public void shouldThrowNewLessonCollidesWithExistingOnesWhenNewLessonCollidesWithExistingOnesAndSubdomainIsOrganization() {
        //given
        LocalDateTime startDateOfLessonStub = LocalDateTime.now();
        LocalDateTime endDateOfLessonStub = startDateOfLessonStub.plusHours(2);
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        String organizationEmailAddressStub = ORGANIZATION_EMAIL_ADDRESS_STUB;
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Lists.newArrayList(IndividualLessonEntityStub.createWithDatesOfLesson(startDateOfLessonStub, endDateOfLessonStub));

        when(individualLessonRepositoryMock.findAllIndividualLessonsWhichCanCollideWithNewIndividualLessonForOrganizationAsSubdomain(eq(startDateOfLessonStub), eq(endDateOfLessonStub), eq(tutorEmailAddressStub), eq(organizationEmailAddressStub))).thenReturn(existingIndividualLessonEntitiesStub);

        //when
        try {
            testee.validateIfNewLessonDoesNotCollideWithExistingOnes(startDateOfLessonStub, endDateOfLessonStub, tutorEmailAddressStub, organizationEmailAddressStub);
        } catch (NewLessonCollidesWithExistingOnes e) {

            //then exception is thrown
            verify(individualLessonRepositoryMock).findAllIndividualLessonsWhichCanCollideWithNewIndividualLessonForOrganizationAsSubdomain(eq(startDateOfLessonStub), eq(endDateOfLessonStub), eq(tutorEmailAddressStub), eq(organizationEmailAddressStub));
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenNewLessonDoesNotCollideWithExistingOnesAndSubdomainIsTutor() {
        //given
        LocalDateTime startDateOfLessonStub = LocalDateTime.now();
        LocalDateTime endDateOfLessonStub = startDateOfLessonStub.plusHours(2);
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        String organizationEmailAddressStub = null;
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Collections.emptyList();

        when(individualLessonRepositoryMock.findAllIndividualLessonsWhichCanCollideWithNewIndividualLessonForTutorAsSubdomain(eq(startDateOfLessonStub), eq(endDateOfLessonStub), eq(tutorEmailAddressStub))).thenReturn(existingIndividualLessonEntitiesStub);

        //when
        testee.validateIfNewLessonDoesNotCollideWithExistingOnes(startDateOfLessonStub, endDateOfLessonStub, tutorEmailAddressStub, organizationEmailAddressStub);

        //then nothing happens
        verify(individualLessonRepositoryMock).findAllIndividualLessonsWhichCanCollideWithNewIndividualLessonForTutorAsSubdomain(eq(startDateOfLessonStub), eq(endDateOfLessonStub), eq(tutorEmailAddressStub));
    }

    @Test
    public void shouldDoNothingWhenNewLessonDoesNotCollideWithExistingOnesAndSubdomainIsOrganization() {
        //given
        LocalDateTime startDateOfLessonStub = LocalDateTime.now();
        LocalDateTime endDateOfLessonStub = startDateOfLessonStub.plusHours(2);
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        String organizationEmailAddressStub = ORGANIZATION_EMAIL_ADDRESS_STUB;
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Collections.emptyList();

        when(individualLessonRepositoryMock.findAllIndividualLessonsWhichCanCollideWithNewIndividualLessonForOrganizationAsSubdomain(eq(startDateOfLessonStub), eq(endDateOfLessonStub), eq(tutorEmailAddressStub), eq(organizationEmailAddressStub))).thenReturn(existingIndividualLessonEntitiesStub);

        //when
        testee.validateIfNewLessonDoesNotCollideWithExistingOnes(startDateOfLessonStub, endDateOfLessonStub, tutorEmailAddressStub, organizationEmailAddressStub);

        //then nothing happens
        verify(individualLessonRepositoryMock).findAllIndividualLessonsWhichCanCollideWithNewIndividualLessonForOrganizationAsSubdomain(eq(startDateOfLessonStub), eq(endDateOfLessonStub), eq(tutorEmailAddressStub), eq(organizationEmailAddressStub));
    }
}