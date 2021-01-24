package com.github.sankowskiwojciech.courseslessons.service.individuallesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.*;
import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.StudentNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.SubdomainNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.NewLessonCollidesWithExistingOnesException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToAccessSubdomainException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateLessonException;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.SubdomainType;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.*;
import com.github.sankowskiwojciech.courseslessons.stub.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class IndividualLessonValidatorServiceImplTest {

    private final TutorRepository tutorRepositoryMock = mock(TutorRepository.class);
    private final StudentRepository studentRepositoryMock = mock(StudentRepository.class);
    private final SubdomainService subdomainServiceMock = mock(SubdomainService.class);
    private final OrganizationRepository organizationRepositoryMock = mock(OrganizationRepository.class);
    private final LessonCollisionValidatorService lessonCollisionValidatorServiceMock = mock(LessonCollisionValidatorService.class);
    private final LessonFileValidatorService lessonFileValidatorServiceMock = mock(LessonFileValidatorService.class);
    private final FileAccessPermissionValidatorService fileAccessPermissionValidatorServiceMock = mock(FileAccessPermissionValidatorService.class);
    private final IndividualLessonValidatorService testee = new IndividualLessonValidatorServiceImpl(tutorRepositoryMock, studentRepositoryMock, subdomainServiceMock, organizationRepositoryMock, lessonCollisionValidatorServiceMock, lessonFileValidatorServiceMock, fileAccessPermissionValidatorServiceMock);

    @Before
    public void reset() {
        Mockito.reset(tutorRepositoryMock, studentRepositoryMock, subdomainServiceMock, organizationRepositoryMock, lessonCollisionValidatorServiceMock, lessonFileValidatorServiceMock, fileAccessPermissionValidatorServiceMock);
    }

    @Test(expected = SubdomainNotFoundException.class)
    public void shouldThrowSubdomainNotFoundExceptionWhenSubdomainIsNotFound() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()))).thenThrow(SubdomainNotFoundException.class);

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (SubdomainNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToCreateLessonException.class)
    public void shouldThrowUserNotAllowedToCreateLessionExceptionWhenTutorIsNotFound() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.empty());

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (UserNotAllowedToCreateLessonException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToAccessSubdomainException.class)
    public void shouldThrowUserNotAllowedToAccessSubdomainExceptionWhenTutorDoesNotBelongToProvidedSubdomain() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        StudentEntity studentEntityStub = StudentEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        when(studentRepositoryMock.findById(eq(individualLessonRequestStub.getStudentId()))).thenReturn(Optional.of(studentEntityStub));
        doThrow(UserNotAllowedToAccessSubdomainException.class).when(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(eq(individualLessonRequestStub.getSubdomainAlias()), eq(tutorEntityStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));

        //whenw
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (UserNotAllowedToCreateLessonException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
            verify(studentRepositoryMock).findById(eq(individualLessonRequestStub.getStudentId()));
            verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
            verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(eq(individualLessonRequestStub.getSubdomainAlias()), eq(tutorEntityStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));
            throw e;
        }
    }

    @Test(expected = StudentNotFoundException.class)
    public void shouldThrowStudentNotFoundExceptionWhenStudentIsNotFound() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.TUTOR);
        TutorEntity tutorEntityStub = TutorEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        when(studentRepositoryMock.findById(eq(individualLessonRequestStub.getStudentId()))).thenReturn(Optional.empty());

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (StudentNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()));
            verifyNoInteractions(organizationRepositoryMock);
            verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
            verify(studentRepositoryMock).findById(eq(individualLessonRequestStub.getStudentId()));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToAccessSubdomainException.class)
    public void shouldThrowUserNotAllowedToAccessSubdomainExceptionWhenStudentDoesNotBelongToProvidedSubdomain() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        StudentEntity studentEntityStub = StudentEntityStub.create();
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        when(studentRepositoryMock.findById(eq(individualLessonRequestStub.getStudentId()))).thenReturn(Optional.of(studentEntityStub));
        doThrow(UserNotAllowedToAccessSubdomainException.class).when(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(eq(individualLessonRequestStub.getSubdomainAlias()), eq(tutorEntityStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (StudentNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
            verify(studentRepositoryMock).findById(eq(individualLessonRequestStub.getStudentId()));
            verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(eq(individualLessonRequestStub.getSubdomainAlias()), eq(tutorEntityStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));
            throw e;
        }
    }

    @Test(expected = NewLessonCollidesWithExistingOnesException.class)
    public void shouldThrowNewLessonCollidesWithCurrentOnesWhenNewLessonCollidesWithExistingOnes() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        StudentEntity studentEntityStub = StudentEntityStub.create();
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        when(studentRepositoryMock.findById(eq(individualLessonRequestStub.getStudentId()))).thenReturn(Optional.of(studentEntityStub));
        doThrow(NewLessonCollidesWithExistingOnesException.class).when(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(eq(individualLessonRequestStub.getStartDateOfLesson()), eq(individualLessonRequestStub.getEndDateOfLesson()), eq(tutorEntityStub.getEmailAddress()), eq(organizationEntityStub.getEmailAddress()));

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (NewLessonCollidesWithExistingOnesException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
            verify(studentRepositoryMock).findById(eq(individualLessonRequestStub.getStudentId()));
            verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(eq(individualLessonRequestStub.getSubdomainAlias()), eq(tutorEntityStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));
            verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(eq(individualLessonRequestStub.getStartDateOfLesson()), eq(individualLessonRequestStub.getEndDateOfLesson()), eq(tutorEntityStub.getEmailAddress()), eq(organizationEntityStub.getEmailAddress()));
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenIndividualLessonRequestIsCorrect() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        StudentEntity studentEntityStub = StudentEntityStub.create();
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        when(studentRepositoryMock.findById(eq(individualLessonRequestStub.getStudentId()))).thenReturn(Optional.of(studentEntityStub));

        //when
        IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformation(eq(individualLessonRequestStub.getSubdomainAlias()));
        verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
        verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
        verify(studentRepositoryMock).findById(eq(individualLessonRequestStub.getStudentId()));
        verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(eq(individualLessonRequestStub.getSubdomainAlias()), eq(tutorEntityStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(eq(individualLessonRequestStub.getStartDateOfLesson()), eq(individualLessonRequestStub.getEndDateOfLesson()), eq(tutorEntityStub.getEmailAddress()), eq(organizationEntityStub.getEmailAddress()));
        verify(lessonFileValidatorServiceMock).validateIfFileExists(anyString());
        verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(eq(individualLessonRequestStub.getTutorId()), anyString());

        assertIndividualLesson(individualLesson, individualLessonRequestStub, organizationEntityStub, tutorEntityStub, studentEntityStub);
    }

    @Test
    public void shouldDoNothingWhenIndividualLessonsScheduleRequestIsCorrect() {
        //given
        final Long allLessonsDurationInMinutesStub = 360L;
        IndividualLessonsScheduleRequest individualLessonsScheduleRequestStub = IndividualLessonsScheduleRequestStub.createWithScheduleTypeFixedDurationLessons(allLessonsDurationInMinutesStub);
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        StudentEntity studentEntityStub = StudentEntityStub.create();
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(individualLessonsScheduleRequestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonsScheduleRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        when(studentRepositoryMock.findById(eq(individualLessonsScheduleRequestStub.getStudentId()))).thenReturn(Optional.of(studentEntityStub));

        //when
        IndividualLessonsSchedule individualLessonsSchedule = testee.validateIndividualLessonsScheduleRequest(individualLessonsScheduleRequestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformation(eq(individualLessonsScheduleRequestStub.getSubdomainAlias()));
        verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
        verify(tutorRepositoryMock).findById(eq(individualLessonsScheduleRequestStub.getTutorId()));
        verify(studentRepositoryMock).findById(eq(individualLessonsScheduleRequestStub.getStudentId()));
        verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(eq(individualLessonsScheduleRequestStub.getSubdomainAlias()), eq(tutorEntityStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));

        assertNotNull(individualLessonsSchedule);
    }

    private void assertIndividualLesson(IndividualLesson individualLesson, IndividualLessonRequest individualLessonRequest, OrganizationEntity organizationEntity, TutorEntity tutorEntity, StudentEntity studentEntity) {
        assertNotNull(individualLesson);
        assertEquals(individualLessonRequest.getTitle(), individualLesson.getTitle());
        assertEquals(individualLessonRequest.getStartDateOfLesson(), individualLesson.getStartDateOfLesson());
        assertEquals(individualLessonRequest.getEndDateOfLesson(), individualLesson.getEndDateOfLesson());
        assertEquals(individualLessonRequest.getDescription(), individualLesson.getDescription());
        assertEquals(organizationEntity, individualLesson.getOrganizationEntity());
        assertEquals(tutorEntity, individualLesson.getTutorEntity());
        assertEquals(studentEntity, individualLesson.getStudentEntity());
    }
}