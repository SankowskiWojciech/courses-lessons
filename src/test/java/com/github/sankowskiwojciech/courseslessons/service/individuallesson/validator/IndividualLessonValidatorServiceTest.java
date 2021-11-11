package com.github.sankowskiwojciech.courseslessons.service.individuallesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.subdomain.SubdomainEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.StudentNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.SubdomainNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.file.FileNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.NewLessonCollidesWithExistingOnesException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToAccessFileException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToAccessSubdomainException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateLessonException;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.FileAccessPermissionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonCollisionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonFileValidatorService;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonRequestStub;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonsScheduleRequestStub;
import com.github.sankowskiwojciech.coursestestlib.stub.StudentEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.SubdomainEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.TutorEntityStub;
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
import static org.mockito.Mockito.when;

public class IndividualLessonValidatorServiceTest {
    private final TutorRepository tutorRepositoryMock = mock(TutorRepository.class);
    private final StudentRepository studentRepositoryMock = mock(StudentRepository.class);
    private final SubdomainService subdomainServiceMock = mock(SubdomainService.class);
    private final LessonCollisionValidatorService lessonCollisionValidatorServiceMock = mock(LessonCollisionValidatorService.class);
    private final LessonFileValidatorService lessonFileValidatorServiceMock = mock(LessonFileValidatorService.class);
    private final FileAccessPermissionValidatorService fileAccessPermissionValidatorServiceMock = mock(FileAccessPermissionValidatorService.class);
    private final IndividualLessonValidatorService testee = new IndividualLessonValidatorService(tutorRepositoryMock, subdomainServiceMock, lessonCollisionValidatorServiceMock, lessonFileValidatorServiceMock, fileAccessPermissionValidatorServiceMock, studentRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(tutorRepositoryMock, studentRepositoryMock, subdomainServiceMock, lessonCollisionValidatorServiceMock, lessonFileValidatorServiceMock, fileAccessPermissionValidatorServiceMock);
    }

    @Test(expected = SubdomainNotFoundException.class)
    public void shouldThrowSubdomainNotFoundExceptionWhenSubdomainIsNotFound() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        String userId = tutorStub.getEmailAddress();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenThrow(SubdomainNotFoundException.class);

        //when
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub, userId);
        } catch (SubdomainNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToCreateLessonException.class)
    public void shouldThrowUserNotAllowedToCreateLessonExceptionWhenTutorIsNotFound() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        String userId = tutorStub.getEmailAddress();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        //when
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub, userId);
        } catch (UserNotAllowedToCreateLessonException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            verify(tutorRepositoryMock).findById(userId);
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToAccessSubdomainException.class)
    public void shouldThrowUserNotAllowedToAccessSubdomainExceptionWhenTutorDoesNotHaveAccessToProvidedSubdomain() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        String userId = tutorStub.getEmailAddress();
        StudentEntity studentStub = StudentEntityStub.create();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        when(studentRepositoryMock.findById(requestStub.getStudentId())).thenReturn(Optional.of(studentStub));
        doThrow(UserNotAllowedToAccessSubdomainException.class).when(subdomainServiceMock).validateIfUserHasAccessToSubdomain(requestStub.getSubdomainAlias(), tutorStub.getEmailAddress(), studentStub.getEmailAddress());

        //whenw
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub, userId);
        } catch (UserNotAllowedToCreateLessonException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            verify(tutorRepositoryMock).findById(userId);
            verify(studentRepositoryMock).findById(requestStub.getStudentId());
            verify(subdomainServiceMock).validateIfUserHasAccessToSubdomain(requestStub.getSubdomainAlias(), tutorStub.getEmailAddress(), studentStub.getEmailAddress());
            throw e;
        }
    }

    @Test(expected = StudentNotFoundException.class)
    public void shouldThrowStudentNotFoundExceptionWhenStudentIsNotFound() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        String userId = tutorStub.getEmailAddress();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        when(studentRepositoryMock.findById(requestStub.getStudentId())).thenReturn(Optional.empty());

        //when
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub, userId);
        } catch (StudentNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            verify(tutorRepositoryMock).findById(userId);
            verify(studentRepositoryMock).findById(requestStub.getStudentId());
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToAccessSubdomainException.class)
    public void shouldThrowUserNotAllowedToAccessSubdomainExceptionWhenStudentDoesNotHaveAccessToProvidedSubdomain() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        String userId = tutorStub.getEmailAddress();
        StudentEntity studentStub = StudentEntityStub.create();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        when(studentRepositoryMock.findById(requestStub.getStudentId())).thenReturn(Optional.of(studentStub));
        doThrow(UserNotAllowedToAccessSubdomainException.class).when(subdomainServiceMock).validateIfUserHasAccessToSubdomain(requestStub.getSubdomainAlias(), tutorStub.getEmailAddress(), studentStub.getEmailAddress());

        //when
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub, userId);
        } catch (StudentNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            verify(tutorRepositoryMock).findById(userId);
            verify(studentRepositoryMock).findById(requestStub.getStudentId());
            verify(subdomainServiceMock).validateIfUserHasAccessToSubdomain(requestStub.getSubdomainAlias(), tutorStub.getEmailAddress(), studentStub.getEmailAddress());
            throw e;
        }
    }

    @Test(expected = NewLessonCollidesWithExistingOnesException.class)
    public void shouldThrowNewLessonCollidesWithCurrentOnesWhenNewLessonCollidesWithExistingOnes() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        String userId = tutorStub.getEmailAddress();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        doThrow(NewLessonCollidesWithExistingOnesException.class).when(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), tutorStub.getEmailAddress());

        //when
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub, userId);
        } catch (NewLessonCollidesWithExistingOnesException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            verify(tutorRepositoryMock).findById(userId);
            verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), tutorStub.getEmailAddress());
            throw e;
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowFileNotFoundExceptionWhenFileWithGivenFileIdIsNotFound() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        String userId = tutorStub.getEmailAddress();
        StudentEntity studentStub = StudentEntityStub.create();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        when(studentRepositoryMock.findById(requestStub.getStudentId())).thenReturn(Optional.of(studentStub));
        doThrow(FileNotFoundException.class).when(lessonFileValidatorServiceMock).validateIfFileExists(requestStub.getFilesIds().get(0));

        //when
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub, userId);
        } catch (FileNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            verify(tutorRepositoryMock).findById(userId);
            verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), userId);
            verify(lessonFileValidatorServiceMock).validateIfFileExists(requestStub.getFilesIds().get(0));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToAccessFileException.class)
    public void shouldThrowUserNotAllowedToAccessFileExceptionWhenTutorDoesNotHaveAccessToFile() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        String userId = tutorStub.getEmailAddress();
        StudentEntity studentStub = StudentEntityStub.create();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        when(studentRepositoryMock.findById(requestStub.getStudentId())).thenReturn(Optional.of(studentStub));
        doThrow(UserNotAllowedToAccessFileException.class).when(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(userId, requestStub.getFilesIds().get(0));

        //when
        try {
            IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub, userId);
        } catch (UserNotAllowedToAccessFileException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            verify(tutorRepositoryMock).findById(userId);
            verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), userId);
            verify(lessonFileValidatorServiceMock).validateIfFileExists(requestStub.getFilesIds().get(0));
            verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(userId, requestStub.getFilesIds().get(0));
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenIndividualLessonRequestIsCorrect() {
        //given
        IndividualLessonRequest requestStub = IndividualLessonRequestStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        String userId = tutorStub.getEmailAddress();
        StudentEntity studentStub = StudentEntityStub.create();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        when(studentRepositoryMock.findById(requestStub.getStudentId())).thenReturn(Optional.of(studentStub));

        //when
        IndividualLesson lesson = testee.validateCreateIndividualLessonRequest(requestStub, userId);

        //then
        verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
        verify(tutorRepositoryMock).findById(userId);
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), tutorStub.getEmailAddress());
        verify(lessonFileValidatorServiceMock).validateIfFileExists(anyString());
        verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(eq(userId), anyString());
        verify(studentRepositoryMock).findById(requestStub.getStudentId());
        verify(subdomainServiceMock).validateIfUserHasAccessToSubdomain(requestStub.getSubdomainAlias(), tutorStub.getEmailAddress(), studentStub.getEmailAddress());

        assertIndividualLesson(lesson, requestStub, subdomainStub, tutorStub, studentStub);
    }

    @Test
    public void shouldDoNothingWhenIndividualLessonsScheduleRequestIsCorrect() {
        //given
        final Long durationOfAllLessonsInMinutes = 360L;
        IndividualLessonsScheduleRequest requestStub = IndividualLessonsScheduleRequestStub.createWithScheduleTypeFixedDurationLessons(durationOfAllLessonsInMinutes);
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        String userId = tutorStub.getEmailAddress();
        StudentEntity studentStub = StudentEntityStub.create();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        when(studentRepositoryMock.findById(requestStub.getStudentId())).thenReturn(Optional.of(studentStub));

        //when
        IndividualLessonsSchedule schedule = testee.validateIndividualLessonsScheduleRequest(requestStub, userId);

        //then
        verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
        verify(tutorRepositoryMock).findById(userId);
        verify(studentRepositoryMock).findById(requestStub.getStudentId());
        verify(subdomainServiceMock).validateIfUserHasAccessToSubdomain(requestStub.getSubdomainAlias(), tutorStub.getEmailAddress(), studentStub.getEmailAddress());

        assertNotNull(schedule);
    }

    private void assertIndividualLesson(IndividualLesson lesson, IndividualLessonRequest request, SubdomainEntity subdomain, TutorEntity tutor, StudentEntity student) {
        assertNotNull(lesson);
        assertEquals(request.getTitle(), lesson.getTitle());
        assertEquals(request.getStartDate(), lesson.getStartDate());
        assertEquals(request.getEndDate(), lesson.getEndDate());
        assertEquals(request.getDescription(), lesson.getDescription());
        assertEquals(subdomain, lesson.getSubdomainEntity());
        assertEquals(tutor, lesson.getTutorEntity());
        assertEquals(student, lesson.getStudentEntity());
    }
}