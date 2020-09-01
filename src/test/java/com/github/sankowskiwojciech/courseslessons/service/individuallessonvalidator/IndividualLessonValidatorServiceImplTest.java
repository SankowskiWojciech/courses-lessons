package com.github.sankowskiwojciech.courseslessons.service.individuallessonvalidator;

import com.github.sankowskiwojciech.courseslessons.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.courseslessons.backend.repository.OrganizationRepository;
import com.github.sankowskiwojciech.courseslessons.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.courseslessons.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.courseslessons.model.exception.NewLessonCollidesWithExistingOnes;
import com.github.sankowskiwojciech.courseslessons.model.exception.StudentNotFoundException;
import com.github.sankowskiwojciech.courseslessons.model.exception.SubdomainNotFoundException;
import com.github.sankowskiwojciech.courseslessons.model.exception.UserNotAllowedToAccessSubdomainException;
import com.github.sankowskiwojciech.courseslessons.model.exception.UserNotAllowedToCreateLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.request.IndividualLessonRequest;
import com.github.sankowskiwojciech.courseslessons.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.courseslessons.model.subdomain.SubdomainType;
import com.github.sankowskiwojciech.courseslessons.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonRequestStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.SubdomainStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
    private final IndividualLessonRepository individualLessonRepositoryMock = mock(IndividualLessonRepository.class);
    private final IndividualLessonValidatorService testee = new IndividualLessonValidatorServiceImpl(tutorRepositoryMock, studentRepositoryMock, subdomainServiceMock, organizationRepositoryMock, individualLessonRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(tutorRepositoryMock, studentRepositoryMock, subdomainServiceMock, organizationRepositoryMock, individualLessonRepositoryMock);
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
        } catch (UserNotAllowedToCreateLesson e) {

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

    @Test(expected = NewLessonCollidesWithExistingOnes.class)
    public void shouldThrowNewLessonCollidesWithCurrentOnesWhenNewLessonCollidesWithExistingOnes() {
        //given
        List<IndividualLessonEntity> existingIndividualLessonEntitiesStub = Lists.newArrayList(IndividualLessonEntityStub.createWithDatesOfLesson(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1)));
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        StudentEntity studentEntityStub = StudentEntityStub.create();
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        when(studentRepositoryMock.findById(eq(individualLessonRequestStub.getStudentId()))).thenReturn(Optional.of(studentEntityStub));
        when(individualLessonRepositoryMock.findAllIndividualLessonsWhichCanCollideWithNewIndividualLesson(eq(individualLessonRequestStub.getStartDateOfLesson()), eq(individualLessonRequestStub.getEndDateOfLesson()), eq(tutorEntityStub.getEmailAddress()))).thenReturn(existingIndividualLessonEntitiesStub);

        //when
        try {
            IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);
        } catch (NewLessonCollidesWithExistingOnes e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(tutorEntityStub.getEmailAddress()));
            verify(studentRepositoryMock).findById(eq(individualLessonRequestStub.getStudentId()));
            verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));
            verify(individualLessonRepositoryMock).findAllIndividualLessonsWhichCanCollideWithNewIndividualLesson(eq(individualLessonRequestStub.getStartDateOfLesson()), eq(individualLessonRequestStub.getEndDateOfLesson()), eq(tutorEntityStub.getEmailAddress()));
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenIndividualLessonRequestIsCorrectAndSubdomainTypeIsOrganization() {
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
        when(individualLessonRepositoryMock.findAllIndividualLessonsWhichCanCollideWithNewIndividualLesson(eq(individualLessonRequestStub.getStartDateOfLesson()), eq(individualLessonRequestStub.getEndDateOfLesson()), eq(tutorEntityStub.getEmailAddress()))).thenReturn(Collections.emptyList());

        //when
        IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
        verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
        verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(tutorEntityStub.getEmailAddress()));
        verify(studentRepositoryMock).findById(eq(individualLessonRequestStub.getStudentId()));
        verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));
        verify(individualLessonRepositoryMock).findAllIndividualLessonsWhichCanCollideWithNewIndividualLesson(eq(individualLessonRequestStub.getStartDateOfLesson()), eq(individualLessonRequestStub.getEndDateOfLesson()), eq(tutorEntityStub.getEmailAddress()));
        assertIndividualLesson(individualLesson, individualLessonRequestStub, organizationEntityStub, tutorEntityStub, studentEntityStub);
    }

    @Test
    public void shouldDoNothingWhenIndividualLessonRequestIsCorrectAndSubdomainTypeIsTutor() {
        //given
        IndividualLessonRequest individualLessonRequestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.TUTOR);
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        StudentEntity studentEntityStub = StudentEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()))).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(eq(individualLessonRequestStub.getTutorId()))).thenReturn(Optional.of(tutorEntityStub));
        when(studentRepositoryMock.findById(eq(individualLessonRequestStub.getStudentId()))).thenReturn(Optional.of(studentEntityStub));
        when(individualLessonRepositoryMock.findAllIndividualLessonsWhichCanCollideWithNewIndividualLesson(eq(individualLessonRequestStub.getStartDateOfLesson()), eq(individualLessonRequestStub.getEndDateOfLesson()), eq(tutorEntityStub.getEmailAddress()))).thenReturn(Collections.emptyList());

        //when
        IndividualLesson individualLesson = testee.validateCreateIndividualLessonRequest(individualLessonRequestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformationIfSubdomainExists(eq(individualLessonRequestStub.getSubdomainName()));
        verifyNoInteractions(organizationRepositoryMock);
        verify(tutorRepositoryMock).findById(eq(individualLessonRequestStub.getTutorId()));
        verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(tutorEntityStub.getEmailAddress()));
        verify(studentRepositoryMock).findById(eq(individualLessonRequestStub.getStudentId()));
        verify(subdomainServiceMock).validateIfUserIsAllowedToAccessSubdomain(eq(subdomainStub.getEmailAddress()), eq(studentEntityStub.getEmailAddress()));
        verify(individualLessonRepositoryMock).findAllIndividualLessonsWhichCanCollideWithNewIndividualLesson(eq(individualLessonRequestStub.getStartDateOfLesson()), eq(individualLessonRequestStub.getEndDateOfLesson()), eq(tutorEntityStub.getEmailAddress()));
        assertIndividualLesson(individualLesson, individualLessonRequestStub, null, tutorEntityStub, studentEntityStub);
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