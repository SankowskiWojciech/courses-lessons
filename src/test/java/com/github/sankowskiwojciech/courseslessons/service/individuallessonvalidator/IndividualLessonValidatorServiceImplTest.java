package com.github.sankowskiwojciech.courseslessons.service.individuallessonvalidator;

import com.github.sankowskiwojciech.courseslessons.backend.repository.OrganizationRepository;
import com.github.sankowskiwojciech.courseslessons.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.courseslessons.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.courseslessons.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.courseslessons.model.exception.NewLessonCollidesWithExistingOnesDetailedException;
import com.github.sankowskiwojciech.courseslessons.model.exception.StudentNotFoundDetailedException;
import com.github.sankowskiwojciech.courseslessons.model.exception.SubdomainNotFoundDetailedException;
import com.github.sankowskiwojciech.courseslessons.model.exception.UserNotAllowedToAccessSubdomainDetailedException;
import com.github.sankowskiwojciech.courseslessons.model.exception.UserNotAllowedToCreateLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.request.IndividualLessonRequest;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.courseslessons.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.courseslessons.model.subdomain.SubdomainType;
import com.github.sankowskiwojciech.courseslessons.service.lessonvalidator.LessonCollisionValidatorService;
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

    @Test(expected = SubdomainNotFoundDetailedException.class)
    public void shouldThrowSubdomainNotFoundExceptionWhenSubdomainIsNotFound() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();

        when(subdomainServiceMock.readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()))).thenThrow(SubdomainNotFoundDetailedException.class);

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (SubdomainNotFoundDetailedException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToCreateLesson.class)
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
        } catch (UserNotAllowedToCreateLesson e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToAccessSubdomainDetailedException.class)
    public void shouldThrowUserNotAllowedToAccessSubdomainExceptionWhenTutorDoesNotBelongToProvidedSubdomain() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        doThrow(UserNotAllowedToAccessSubdomainDetailedException.class).when(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(tutorEntityStub.getEmailAddress()));

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (UserNotAllowedToCreateLesson e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
            verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(tutorEntityStub.getEmailAddress()));
            throw e;
        }
    }

    @Test(expected = StudentNotFoundDetailedException.class)
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
        } catch (StudentNotFoundDetailedException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
            verifyNoInteractions(organizationRepositoryMock);
            verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
            verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(tutorEntityStub.getEmailAddress()));
            verify(studentRepositoryMock).findById(eq(individualLessonRequestStub.getStudentId()));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToAccessSubdomainDetailedException.class)
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
        doThrow(UserNotAllowedToAccessSubdomainDetailedException.class).when(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (StudentNotFoundDetailedException e) {

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

    @Test(expected = NewLessonCollidesWithExistingOnesDetailedException.class)
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
        doThrow(NewLessonCollidesWithExistingOnesDetailedException.class).when(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(eq(individualLessonRequestStub.getStartDateOfLesson()), eq(individualLessonRequestStub.getEndDateOfLesson()), eq(tutorEntityStub.getEmailAddress()), eq(organizationEntityStub.getEmailAddress()));

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (NewLessonCollidesWithExistingOnesDetailedException e) {

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