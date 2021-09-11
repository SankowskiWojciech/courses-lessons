package com.github.sankowskiwojciech.courseslessons.service.grouplesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.*;
import com.github.sankowskiwojciech.coursescorelib.model.db.group.StudentsGroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.group.GroupNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateLessonException;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.SubdomainType;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.*;
import com.github.sankowskiwojciech.courseslessons.stub.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class GroupLessonValidatorServiceTest {

    private final TutorRepository tutorRepositoryMock = mock(TutorRepository.class);
    private final StudentRepository studentRepositoryMock = mock(StudentRepository.class);
    private final SubdomainService subdomainServiceMock = mock(SubdomainService.class);
    private final OrganizationRepository organizationRepositoryMock = mock(OrganizationRepository.class);
    private final LessonCollisionValidatorService lessonCollisionValidatorServiceMock = mock(LessonCollisionValidatorService.class);
    private final LessonFileValidatorService lessonFileValidatorServiceMock = mock(LessonFileValidatorService.class);
    private final FileAccessPermissionValidatorService fileAccessPermissionValidatorServiceMock = mock(FileAccessPermissionValidatorService.class);
    private final StudentsGroupRepository studentsGroupRepositoryMock = mock(StudentsGroupRepository.class);
    private final GroupLessonValidatorService testee = new GroupLessonValidatorService(tutorRepositoryMock, subdomainServiceMock, organizationRepositoryMock, lessonCollisionValidatorServiceMock, lessonFileValidatorServiceMock, fileAccessPermissionValidatorServiceMock, studentsGroupRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(tutorRepositoryMock, studentRepositoryMock, subdomainServiceMock, organizationRepositoryMock, lessonCollisionValidatorServiceMock, lessonFileValidatorServiceMock, fileAccessPermissionValidatorServiceMock, studentsGroupRepositoryMock);
    }


    @Test(expected = GroupNotFoundException.class)
    public void shouldThrowGroupNotFoundExceptionWhenGroupWithGivenGroupIdDoesNotExistInDatabase() {
        //given
        GroupLessonRequest groupLessonRequestStub = GroupLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(groupLessonRequestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(subdomainStub.getEmailAddress())).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(groupLessonRequestStub.getTutorId())).thenReturn(Optional.of(tutorEntityStub));
        when(studentsGroupRepositoryMock.findById(groupLessonRequestStub.getGroupId())).thenReturn(Optional.empty());

        //when
        GroupLesson groupLesson = testee.validateCreateGroupLessonRequest(groupLessonRequestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformation(groupLessonRequestStub.getSubdomainAlias());
        verify(organizationRepositoryMock).findById(subdomainStub.getEmailAddress());
        verify(tutorRepositoryMock).findById(groupLessonRequestStub.getTutorId());
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(groupLessonRequestStub.getStartDateOfLesson(), groupLessonRequestStub.getEndDateOfLesson(), tutorEntityStub.getEmailAddress());
        verify(lessonFileValidatorServiceMock).validateIfFileExists(anyString());
        verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(groupLessonRequestStub.getTutorId(), anyString());
        verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(groupLessonRequestStub.getSubdomainAlias(), tutorEntityStub.getEmailAddress());
        verify(studentsGroupRepositoryMock).findById(groupLessonRequestStub.getGroupId());
    }

    @Test(expected = UserNotAllowedToCreateLessonException.class)
    public void shouldThrowUserNotAllowedToCreateLessonExceptionWhenTutorIsNotTheOwnerOfTheGroup() {
        //given
        GroupLessonRequest groupLessonRequestStub = GroupLessonRequestStub.create();
        TutorEntity tutorEntityStub = TutorEntityStub.createWithTutorId(UUID.randomUUID().toString());
        StudentsGroupEntity groupEntityStub = StudentsGroupEntityStub.createWithTutorEntity(tutorEntityStub);
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(groupLessonRequestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(subdomainStub.getEmailAddress())).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(groupLessonRequestStub.getTutorId())).thenReturn(Optional.of(tutorEntityStub));
        when(studentsGroupRepositoryMock.findById(groupLessonRequestStub.getGroupId())).thenReturn(Optional.of(groupEntityStub));

        //when
        GroupLesson groupLesson = testee.validateCreateGroupLessonRequest(groupLessonRequestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformation(groupLessonRequestStub.getSubdomainAlias());
        verify(organizationRepositoryMock).findById(subdomainStub.getEmailAddress());
        verify(tutorRepositoryMock).findById(groupLessonRequestStub.getTutorId());
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(groupLessonRequestStub.getStartDateOfLesson(), groupLessonRequestStub.getEndDateOfLesson(), tutorEntityStub.getEmailAddress());
        verify(lessonFileValidatorServiceMock).validateIfFileExists(anyString());
        verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(groupLessonRequestStub.getTutorId(), anyString());
        verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(groupLessonRequestStub.getSubdomainAlias(), tutorEntityStub.getEmailAddress());
        verify(studentsGroupRepositoryMock).findById(groupLessonRequestStub.getGroupId());
    }

    @Test
    public void shouldDoNothingWhenGroupLessonRequestIsCorrect() {
        //given
        GroupLessonRequest groupLessonRequestStub = GroupLessonRequestStub.create();
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        StudentsGroupEntity groupEntityStub = StudentsGroupEntityStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(groupLessonRequestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(subdomainStub.getEmailAddress())).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(groupLessonRequestStub.getTutorId())).thenReturn(Optional.of(tutorEntityStub));
        when(studentsGroupRepositoryMock.findById(groupLessonRequestStub.getGroupId())).thenReturn(Optional.of(groupEntityStub));

        //when
        GroupLesson groupLesson = testee.validateCreateGroupLessonRequest(groupLessonRequestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformation(groupLessonRequestStub.getSubdomainAlias());
        verify(organizationRepositoryMock).findById(subdomainStub.getEmailAddress());
        verify(tutorRepositoryMock).findById(groupLessonRequestStub.getTutorId());
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(groupLessonRequestStub.getStartDateOfLesson(), groupLessonRequestStub.getEndDateOfLesson(), tutorEntityStub.getEmailAddress());
        verify(lessonFileValidatorServiceMock).validateIfFileExists(anyString());
        verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(eq(groupLessonRequestStub.getTutorId()), anyString());
        verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(groupLessonRequestStub.getSubdomainAlias(), tutorEntityStub.getEmailAddress());
        verify(studentsGroupRepositoryMock).findById(groupLessonRequestStub.getGroupId());
        assertGroupLesson(groupLesson, groupLessonRequestStub, organizationEntityStub, tutorEntityStub, groupEntityStub);
    }

    private void assertGroupLesson(GroupLesson groupLesson, GroupLessonRequest groupLessonRequest, OrganizationEntity organizationEntity, TutorEntity tutorEntity, StudentsGroupEntity groupEntity) {
        assertNotNull(groupLesson);
        assertEquals(groupLessonRequest.getTitle(), groupLesson.getTitle());
        assertEquals(groupLessonRequest.getStartDateOfLesson(), groupLesson.getStartDateOfLesson());
        assertEquals(groupLessonRequest.getEndDateOfLesson(), groupLesson.getEndDateOfLesson());
        assertEquals(groupLessonRequest.getDescription(), groupLesson.getDescription());
        assertEquals(organizationEntity, groupLesson.getOrganizationEntity());
        assertEquals(tutorEntity, groupLesson.getTutorEntity());
        assertEquals(groupEntity, groupLesson.getStudentsGroupEntity());
    }
}