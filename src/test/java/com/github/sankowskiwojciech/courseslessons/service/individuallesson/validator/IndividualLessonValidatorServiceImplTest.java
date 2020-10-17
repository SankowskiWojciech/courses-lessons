package com.github.sankowskiwojciech.courseslessons.service.individuallesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.OrganizationRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
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
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonCollisionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonRequestStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonsScheduleRequestStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.SubdomainStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class IndividualLessonValidatorServiceImplTest {

    private final TutorRepository tutorRepositoryMock = mock(TutorRepository.class);
    private final StudentRepository studentRepositoryMock = mock(StudentRepository.class);
    private final SubdomainService subdomainServiceMock = mock(SubdomainService.class);
    private final OrganizationRepository organizationRepositoryMock = mock(OrganizationRepository.class);
    private final LessonCollisionValidatorService lessonCollisionValidatorServiceMock = mock(LessonCollisionValidatorService.class);
    private final IndividualLessonValidatorService testee = new IndividualLessonValidatorServiceImpl(tutorRepositoryMock, studentRepositoryMock, subdomainServiceMock, organizationRepositoryMock, lessonCollisionValidatorServiceMock);

    @Before
    public void reset() {
        Mockito.reset(tutorRepositoryMock, studentRepositoryMock, subdomainServiceMock, organizationRepositoryMock, lessonCollisionValidatorServiceMock);
    }

    @Test(expected = SubdomainNotFoundException.class)
    public void shouldThrowSubdomainNotFoundExceptionWhenSubdomainIsNotFound() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();

        when(subdomainServiceMock.readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()))).thenThrow(SubdomainNotFoundException.class);

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (SubdomainNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToCreateLessonException.class)
    public void shouldThrowUserNotAllowedToCreateLessionExceptionWhenTutorIsNotFound() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.empty());

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (UserNotAllowedToCreateLessonException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
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
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        doThrow(UserNotAllowedToAccessSubdomainException.class).when(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(tutorEntityStub.getEmailAddress()));

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (UserNotAllowedToCreateLessonException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
            verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(tutorEntityStub.getEmailAddress()));
            throw e;
        }
    }

    @Test(expected = StudentNotFoundException.class)
    public void shouldThrowStudentNotFoundExceptionWhenStudentIsNotFound() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.TUTOR);
        TutorEntity tutorEntityStub = TutorEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()))).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        when(studentRepositoryMock.findById(eq(individualLessonRequestStub.getStudentId()))).thenReturn(Optional.empty());

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (StudentNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
            verifyNoInteractions(organizationRepositoryMock);
            verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
            verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(tutorEntityStub.getEmailAddress()));
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

        when(subdomainServiceMock.readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        when(studentRepositoryMock.findById(eq(individualLessonRequestStub.getStudentId()))).thenReturn(Optional.of(studentEntityStub));
        doThrow(UserNotAllowedToAccessSubdomainException.class).when(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (StudentNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
            verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(tutorEntityStub.getEmailAddress()));
            verify(studentRepositoryMock).findById(eq(individualLessonRequestStub.getStudentId()));
            verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));
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

        when(subdomainServiceMock.readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        when(studentRepositoryMock.findById(eq(individualLessonRequestStub.getStudentId()))).thenReturn(Optional.of(studentEntityStub));
        doThrow(NewLessonCollidesWithExistingOnesException.class).when(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(eq(individualLessonRequestStub.getStartDateOfLesson()), eq(individualLessonRequestStub.getEndDateOfLesson()), eq(tutorEntityStub.getEmailAddress()), eq(organizationEntityStub.getEmailAddress()));

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (NewLessonCollidesWithExistingOnesException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(tutorEntityStub.getEmailAddress()));
            verify(studentRepositoryMock).findById(eq(individualLessonRequestStub.getStudentId()));
            verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));
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

        when(subdomainServiceMock.readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        when(studentRepositoryMock.findById(eq(individualLessonRequestStub.getStudentId()))).thenReturn(Optional.of(studentEntityStub));

        //when
        IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
        verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
        verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(tutorEntityStub.getEmailAddress()));
        verify(studentRepositoryMock).findById(eq(individualLessonRequestStub.getStudentId()));
        verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(eq(individualLessonRequestStub.getStartDateOfLesson()), eq(individualLessonRequestStub.getEndDateOfLesson()), eq(tutorEntityStub.getEmailAddress()), eq(organizationEntityStub.getEmailAddress()));
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

        when(subdomainServiceMock.readSubdomainInformationIfSubdomainExists(eq(individualLessonsScheduleRequestStub.getSubdomainName()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonsScheduleRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        when(studentRepositoryMock.findById(eq(individualLessonsScheduleRequestStub.getStudentId()))).thenReturn(Optional.of(studentEntityStub));

        //when
        IndividualLessonsSchedule individualLessonsSchedule = testee.validateIndividualLessonsScheduleRequest(individualLessonsScheduleRequestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonsScheduleRequestStub.getSubdomainName()));
        verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
        verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(tutorEntityStub.getEmailAddress()));
        verify(studentRepositoryMock).findById(eq(individualLessonsScheduleRequestStub.getStudentId()));
        verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));
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