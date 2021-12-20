package com.github.sankowskiwojciech.courseslessons.service.grouplesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.subdomain.SubdomainEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.SubdomainNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.file.FileNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.group.GroupNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.lesson.NewLessonCollidesWithExistingOnesException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToAccessFileException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToAccessSubdomainException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateLessonException;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.FileAccessPermissionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonCollisionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonFileValidatorService;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupLessonRequestStub;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupLessonsScheduleRequestStub;
import com.github.sankowskiwojciech.coursestestlib.stub.SubdomainEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.TutorEntityStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupLessonValidatorServiceTest {
    private final TutorRepository tutorRepositoryMock = mock(TutorRepository.class);
    private final StudentRepository studentRepositoryMock = mock(StudentRepository.class);
    private final SubdomainService subdomainServiceMock = mock(SubdomainService.class);
    private final LessonCollisionValidatorService lessonCollisionValidatorServiceMock = mock(LessonCollisionValidatorService.class);
    private final LessonFileValidatorService lessonFileValidatorServiceMock = mock(LessonFileValidatorService.class);
    private final FileAccessPermissionValidatorService fileAccessPermissionValidatorServiceMock = mock(FileAccessPermissionValidatorService.class);
    private final GroupRepository groupRepositoryMock = mock(GroupRepository.class);
    private final GroupLessonValidatorService testee = new GroupLessonValidatorService(tutorRepositoryMock, subdomainServiceMock, lessonCollisionValidatorServiceMock, lessonFileValidatorServiceMock, fileAccessPermissionValidatorServiceMock, groupRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(tutorRepositoryMock, studentRepositoryMock, subdomainServiceMock, lessonCollisionValidatorServiceMock, lessonFileValidatorServiceMock, fileAccessPermissionValidatorServiceMock, groupRepositoryMock);
    }

    @Test(expected = SubdomainNotFoundException.class)
    public void shouldThrowSubdomainNotFoundExceptionWhenSubdomainIsNotFound() {
        //given
        TutorEntity tutorStub = TutorEntityStub.create();
        GroupLessonRequest requestStub = GroupLessonRequestStub.create();
        String userId = tutorStub.getEmailAddress();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenThrow(SubdomainNotFoundException.class);

        //when
        try {
            GroupLesson lesson = testee.validateCreateGroupLessonRequest(requestStub, userId);
        } catch (SubdomainNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToCreateLessonException.class)
    public void shouldThrowUserNotAllowedToCreateLessonExceptionWhenTutorIsNotFound() {
        //given
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        GroupLessonRequest requestStub = GroupLessonRequestStub.create();
        String userId = tutorStub.getEmailAddress();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        //when
        try {
            GroupLesson lesson = testee.validateCreateGroupLessonRequest(requestStub, userId);
        } catch (UserNotAllowedToCreateLessonException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            verify(tutorRepositoryMock).findById(userId);
            throw e;
        }
    }

    @Test(expected = NewLessonCollidesWithExistingOnesException.class)
    public void shouldThrowNewLessonCollidesWithCurrentOnesWhenNewLessonCollidesWithExistingOnes() {
        //given
        GroupLessonRequest requestStub = GroupLessonRequestStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        String userId = tutorStub.getEmailAddress();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        doThrow(NewLessonCollidesWithExistingOnesException.class).when(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), userId);

        //when
        try {
            GroupLesson lesson = testee.validateCreateGroupLessonRequest(requestStub, userId);
        } catch (NewLessonCollidesWithExistingOnesException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            verify(tutorRepositoryMock).findById(userId);
            verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), userId);
            throw e;
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowFileNotFoundExceptionWhenFileWithGivenFileIdIsNotFound() {
        //given
        TutorEntity tutorStub = TutorEntityStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        GroupLessonRequest requestStub = GroupLessonRequestStub.create();
        String userId = tutorStub.getEmailAddress();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        doThrow(FileNotFoundException.class).when(lessonFileValidatorServiceMock).validateIfFileExists(requestStub.getFilesIds().get(0));

        //when
        try {
            GroupLesson lesson = testee.validateCreateGroupLessonRequest(requestStub, userId);
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
        TutorEntity tutorStub = TutorEntityStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        GroupLessonRequest requestStub = GroupLessonRequestStub.create();
        String userId = tutorStub.getEmailAddress();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        doThrow(UserNotAllowedToAccessFileException.class).when(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(userId, requestStub.getFilesIds().get(0));

        //when
        try {
            GroupLesson lesson = testee.validateCreateGroupLessonRequest(requestStub, userId);
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

    @Test(expected = GroupNotFoundException.class)
    public void shouldThrowGroupNotFoundExceptionWhenGroupWithGivenGroupIdDoesNotExistInDatabase() {
        //given
        GroupLessonRequest requestStub = GroupLessonRequestStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        String userId = tutorStub.getEmailAddress();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        when(groupRepositoryMock.findById(requestStub.getGroupId())).thenReturn(Optional.empty());

        //when
        try {
            GroupLesson lesson = testee.validateCreateGroupLessonRequest(requestStub, userId);
        } catch (GroupNotFoundException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            verify(tutorRepositoryMock).findById(userId);
            verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), userId);
            verify(lessonFileValidatorServiceMock).validateIfFileExists(requestStub.getFilesIds().get(0));
            verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(userId, requestStub.getFilesIds().get(0));
            verify(subdomainServiceMock).validateIfUserHasAccessToSubdomain(requestStub.getSubdomainAlias(), userId);
            verify(groupRepositoryMock).findById(requestStub.getGroupId());
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToAccessSubdomainException.class)
    public void shouldThrowUserNotAllowedToAccessSubdomainExceptionWhenTutorDoesNotHaveAccessToSubdomain() {
        //given
        TutorEntity tutorStub = TutorEntityStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        GroupLessonRequest requestStub = GroupLessonRequestStub.create();
        String userId = tutorStub.getEmailAddress();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        when(subdomainServiceMock.validateIfUserHasAccessToSubdomain(requestStub.getSubdomainAlias(), userId)).thenThrow(UserNotAllowedToAccessSubdomainException.class);

        //when
        try {
            GroupLesson lesson = testee.validateCreateGroupLessonRequest(requestStub, userId);
        } catch (UserNotAllowedToAccessSubdomainException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            verify(tutorRepositoryMock).findById(userId);
            verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), userId);
            verify(lessonFileValidatorServiceMock).validateIfFileExists(requestStub.getFilesIds().get(0));
            verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(userId, requestStub.getFilesIds().get(0));
            verify(subdomainServiceMock).validateIfUserHasAccessToSubdomain(requestStub.getSubdomainAlias(), userId);
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToCreateLessonException.class)
    public void shouldThrowUserNotAllowedToCreateLessonExceptionWhenTutorTriesToCreateGroupLessonInSubdomainForWhichGroupDoesNotBelongTo() {
        //given
        TutorEntity tutorStub = TutorEntityStub.create();
        SubdomainEntity subdomainOfGroupStub = SubdomainEntityStub.createWithSubdomainId(UUID.randomUUID().toString());
        GroupEntity groupStub = GroupEntityStub.createWithSubdomain(subdomainOfGroupStub);
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        GroupLessonRequest requestStub = GroupLessonRequestStub.create();
        String userId = tutorStub.getEmailAddress();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        when(groupRepositoryMock.findById(requestStub.getGroupId())).thenReturn(Optional.of(groupStub));

        //when
        try {
            GroupLesson lesson = testee.validateCreateGroupLessonRequest(requestStub, userId);
        } catch (UserNotAllowedToCreateLessonException e) {

            //then exception is thrown
            verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
            verify(tutorRepositoryMock).findById(userId);
            verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), userId);
            verify(lessonFileValidatorServiceMock).validateIfFileExists(requestStub.getFilesIds().get(0));
            verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(userId, requestStub.getFilesIds().get(0));
            verify(subdomainServiceMock).validateIfUserHasAccessToSubdomain(requestStub.getSubdomainAlias(), userId);
            verify(groupRepositoryMock).findById(requestStub.getGroupId());

            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenGroupLessonRequestIsCorrect() {
        //given
        TutorEntity tutorStub = TutorEntityStub.create();
        GroupEntity groupStub = GroupEntityStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        GroupLessonRequest requestStub = GroupLessonRequestStub.create();
        String userId = tutorStub.getEmailAddress();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        when(groupRepositoryMock.findById(requestStub.getGroupId())).thenReturn(Optional.of(groupStub));

        //when
        GroupLesson lesson = testee.validateCreateGroupLessonRequest(requestStub, userId);

        //then
        verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
        verify(tutorRepositoryMock).findById(userId);
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), userId);
        verify(lessonFileValidatorServiceMock).validateIfFileExists(requestStub.getFilesIds().get(0));
        verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(userId, requestStub.getFilesIds().get(0));
        verify(subdomainServiceMock).validateIfUserHasAccessToSubdomain(requestStub.getSubdomainAlias(), userId);
        verify(groupRepositoryMock).findById(requestStub.getGroupId());
        assertGroupLesson(lesson, requestStub, subdomainStub, tutorStub, groupStub);
    }

    @Test
    public void shouldDoNothingWhenGroupLessonsScheduleRequestIsCorrect() {
        //given
        final Long durationOfAllLessonsInMinutes = 360L;
        GroupLessonsScheduleRequest requestStub = GroupLessonsScheduleRequestStub.createWithScheduleTypeFixedDurationLessons(durationOfAllLessonsInMinutes);
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        String userId = tutorStub.getEmailAddress();
        GroupEntity groupStub = GroupEntityStub.create();

        when(subdomainServiceMock.readSubdomain(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(tutorRepositoryMock.findById(userId)).thenReturn(Optional.of(tutorStub));
        when(groupRepositoryMock.findById(requestStub.getGroupId())).thenReturn(Optional.of(groupStub));

        //when
        GroupLessonsSchedule schedule = testee.validateGroupLessonsScheduleRequest(requestStub, userId);

        //then
        verify(subdomainServiceMock).readSubdomain(requestStub.getSubdomainAlias());
        verify(tutorRepositoryMock).findById(userId);
        verify(groupRepositoryMock).findById(requestStub.getGroupId());
        verify(subdomainServiceMock).validateIfUserHasAccessToSubdomain(subdomainStub.getSubdomainId(), tutorStub.getEmailAddress());

        assertNotNull(schedule);
    }

    private void assertGroupLesson(GroupLesson lesson, GroupLessonRequest request, SubdomainEntity subdomain, TutorEntity tutor, GroupEntity group) {
        assertNotNull(lesson);
        assertEquals(request.getTitle(), lesson.getTitle());
        assertEquals(request.getStartDate(), lesson.getStartDate());
        assertEquals(request.getEndDate(), lesson.getEndDate());
        assertEquals(request.getDescription(), lesson.getDescription());
        assertEquals(subdomain, lesson.getSubdomainEntity());
        assertEquals(tutor, lesson.getTutorEntity());
        assertEquals(group, lesson.getGroupEntity());
    }
}