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
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.FileAccessPermissionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonCollisionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonFileValidatorService;
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
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class IndividualLessonValidatorServiceTest {

    private final TutorRepository tutorRepositoryMock = mock(TutorRepository.class);
    private final StudentRepository studentRepositoryMock = mock(StudentRepository.class);
    private final SubdomainService subdomainServiceMock = mock(SubdomainService.class);
    private final OrganizationRepository organizationRepositoryMock = mock(OrganizationRepository.class);
    private final LessonCollisionValidatorService lessonCollisionValidatorServiceMock = mock(LessonCollisionValidatorService.class);
    private final LessonFileValidatorService lessonFileValidatorServiceMock = mock(LessonFileValidatorService.class);
    private final FileAccessPermissionValidatorService fileAccessPermissionValidatorServiceMock = mock(FileAccessPermissionValidatorService.class);
    private final IndividualLessonValidatorService testee = new IndividualLessonValidatorService(tutorRepositoryMock, subdomainServiceMock, organizationRepositoryMock, lessonCollisionValidatorServiceMock, lessonFileValidatorServiceMock, fileAccessPermissionValidatorServiceMock, studentRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(tutorRepositoryMock, studentRepositoryMock, subdomainServiceMock, organizationRepositoryMock, lessonCollisionValidatorServiceMock, lessonFileValidatorServiceMock, fileAccessPermissionValidatorServiceMock);
    }

    @Test(expected = SubdomainNotFoundException.class)
    public void shouldThrowSubdomainNotFoundExceptionWhenSubdomainIsNotFound() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(requestStub.getSubdomainAlias()))).thenThrow(SubdomainNotFoundException.class);

        //when
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub);
        } catch (SubdomainNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformation(eq(requestStub.getSubdomainAlias()));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToCreateLessonException.class)
    public void shouldThrowUserNotAllowedToCreateLessionExceptionWhenTutorIsNotFound() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        OrganizationEntity organizationStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(requestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationStub));
        when(tutorRepositoryMock.findById(eq(requestStub.getTutorId()))).thenReturn(Optional.empty());

        //when
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub);
        } catch (UserNotAllowedToCreateLessonException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformation(eq(requestStub.getSubdomainAlias()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(tutorRepositoryMock).findById(eq(requestStub.getTutorId()));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToAccessSubdomainException.class)
    public void shouldThrowUserNotAllowedToAccessSubdomainExceptionWhenTutorDoesNotBelongToProvidedSubdomain() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        OrganizationEntity organizationStub = OrganizationEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        StudentEntity studentStub = StudentEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(requestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationStub));
        when(tutorRepositoryMock.findById(eq(requestStub.getTutorId()))).thenReturn(Optional.of(tutorStub));
        when(studentRepositoryMock.findById(eq(requestStub.getStudentId()))).thenReturn(Optional.of(studentStub));
        doThrow(UserNotAllowedToAccessSubdomainException.class).when(subdomainServiceMock).validateIfUserHasAccessToSubdomain(eq(requestStub.getSubdomainAlias()), eq(tutorStub.getEmailAddress()), eq(studentStub.getEmailAddress()));

        //whenw
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub);
        } catch (UserNotAllowedToCreateLessonException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformation(eq(requestStub.getSubdomainAlias()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(tutorRepositoryMock).findById(eq(requestStub.getTutorId()));
            verify(tutorRepositoryMock).findById(eq(requestStub.getTutorId()));
            verify(studentRepositoryMock).findById(eq(requestStub.getStudentId()));
            verify(subdomainServiceMock).validateIfUserHasAccessToSubdomain(eq(requestStub.getSubdomainAlias()), eq(tutorStub.getEmailAddress()), eq(studentStub.getEmailAddress()));
            throw e;
        }
    }

    @Test(expected = StudentNotFoundException.class)
    public void shouldThrowStudentNotFoundExceptionWhenStudentIsNotFound() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.TUTOR);
        TutorEntity tutorStub = TutorEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(requestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(eq(requestStub.getTutorId()))).thenReturn(Optional.of(tutorStub));
        when(studentRepositoryMock.findById(eq(requestStub.getStudentId()))).thenReturn(Optional.empty());

        //when
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub);
        } catch (StudentNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformation(eq(requestStub.getSubdomainAlias()));
            verifyNoInteractions(organizationRepositoryMock);
            verify(tutorRepositoryMock).findById(eq(requestStub.getTutorId()));
            verify(studentRepositoryMock).findById(eq(requestStub.getStudentId()));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToAccessSubdomainException.class)
    public void shouldThrowUserNotAllowedToAccessSubdomainExceptionWhenStudentDoesNotBelongToProvidedSubdomain() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        TutorEntity tutorStub = TutorEntityStub.create();
        StudentEntity studentStub = StudentEntityStub.create();
        OrganizationEntity organizationStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(requestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationStub));
        when(tutorRepositoryMock.findById(eq(requestStub.getTutorId()))).thenReturn(Optional.of(tutorStub));
        when(studentRepositoryMock.findById(eq(requestStub.getStudentId()))).thenReturn(Optional.of(studentStub));
        doThrow(UserNotAllowedToAccessSubdomainException.class).when(subdomainServiceMock).validateIfUserHasAccessToSubdomain(eq(requestStub.getSubdomainAlias()), eq(tutorStub.getEmailAddress()), eq(studentStub.getEmailAddress()));

        //when
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub);
        } catch (StudentNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformation(eq(requestStub.getSubdomainAlias()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(tutorRepositoryMock).findById(eq(requestStub.getTutorId()));
            verify(studentRepositoryMock).findById(eq(requestStub.getStudentId()));
            verify(subdomainServiceMock).validateIfUserHasAccessToSubdomain(eq(requestStub.getSubdomainAlias()), eq(tutorStub.getEmailAddress()), eq(studentStub.getEmailAddress()));
            throw e;
        }
    }

    @Test(expected = NewLessonCollidesWithExistingOnesException.class)
    public void shouldThrowNewLessonCollidesWithCurrentOnesWhenNewLessonCollidesWithExistingOnes() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        TutorEntity tutorStub = TutorEntityStub.create();
        OrganizationEntity organizationStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(requestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationStub));
        when(tutorRepositoryMock.findById(eq(requestStub.getTutorId()))).thenReturn(Optional.of(tutorStub));
        doThrow(NewLessonCollidesWithExistingOnesException.class).when(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(eq(requestStub.getStartDate()), eq(requestStub.getEndDate()), eq(tutorStub.getEmailAddress()));

        //when
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub);
        } catch (NewLessonCollidesWithExistingOnesException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomainInformation(eq(requestStub.getSubdomainAlias()));
            verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
            verify(tutorRepositoryMock).findById(eq(requestStub.getTutorId()));
            verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(eq(requestStub.getStartDate()), eq(requestStub.getEndDate()), eq(tutorStub.getEmailAddress()));
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenIndividualLessonRequestIsCorrect() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        TutorEntity tutorStub = TutorEntityStub.create();
        StudentEntity studentStub = StudentEntityStub.create();
        OrganizationEntity organizationStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(requestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationStub));
        when(tutorRepositoryMock.findById(eq(requestStub.getTutorId()))).thenReturn(Optional.of(tutorStub));
        when(studentRepositoryMock.findById(eq(requestStub.getStudentId()))).thenReturn(Optional.of(studentStub));

        //when
        IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformation(eq(requestStub.getSubdomainAlias()));
        verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
        verify(tutorRepositoryMock).findById(eq(requestStub.getTutorId()));
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(eq(requestStub.getStartDate()), eq(requestStub.getEndDate()), eq(tutorStub.getEmailAddress()));
        verify(lessonFileValidatorServiceMock).validateIfFileExists(anyString());
        verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(eq(requestStub.getTutorId()), anyString());
        verify(studentRepositoryMock).findById(eq(requestStub.getStudentId()));
        verify(subdomainServiceMock).validateIfUserHasAccessToSubdomain(eq(requestStub.getSubdomainAlias()), eq(tutorStub.getEmailAddress()), eq(studentStub.getEmailAddress()));

        assertIndividualLesson(lesson, requestStub, organizationStub, tutorStub, studentStub);
    }

    @Test
    public void shouldDoNothingWhenIndividualLessonsScheduleRequestIsCorrect() {
        //given
        final Long durationOfAllLessonsInMinutes = 360L;
        IndividualLessonsScheduleRequest requestStub = IndividualLessonsScheduleRequestStub.createWithScheduleTypeFixedDurationLessons(durationOfAllLessonsInMinutes);
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        TutorEntity tutorStub = TutorEntityStub.create();
        StudentEntity studentStub = StudentEntityStub.create();
        OrganizationEntity organizationStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(eq(requestStub.getSubdomainAlias()))).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(eq(subdomainStub.getEmailAddress()))).thenReturn(Optional.of(organizationStub));
        when(tutorRepositoryMock.findById(eq(requestStub.getTutorId()))).thenReturn(Optional.of(tutorStub));
        when(studentRepositoryMock.findById(eq(requestStub.getStudentId()))).thenReturn(Optional.of(studentStub));

        //when
        IndividualLessonsSchedule schedule = testee.validateIndividualLessonsScheduleRequest(requestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformation(eq(requestStub.getSubdomainAlias()));
        verify(organizationRepositoryMock).findById(eq(subdomainStub.getEmailAddress()));
        verify(tutorRepositoryMock).findById(eq(requestStub.getTutorId()));
        verify(studentRepositoryMock).findById(eq(requestStub.getStudentId()));
        verify(subdomainServiceMock).validateIfUserHasAccessToSubdomain(eq(requestStub.getSubdomainAlias()), eq(tutorStub.getEmailAddress()), eq(studentStub.getEmailAddress()));

        assertNotNull(schedule);
    }

    private void assertIndividualLesson(IndividualLesson lesson, IndividualLessonRequest request, OrganizationEntity organization, TutorEntity tutor, StudentEntity student) {
        assertNotNull(lesson);
        assertEquals(request.getTitle(), lesson.getTitle());
        assertEquals(request.getStartDate(), lesson.getStartDate());
        assertEquals(request.getEndDate(), lesson.getEndDate());
        assertEquals(request.getDescription(), lesson.getDescription());
        assertEquals(organization, lesson.getOrganizationEntity());
        assertEquals(tutor, lesson.getTutorEntity());
        assertEquals(student, lesson.getStudentEntity());
    }
}