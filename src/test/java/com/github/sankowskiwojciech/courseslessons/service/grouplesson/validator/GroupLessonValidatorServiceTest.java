package com.github.sankowskiwojciech.courseslessons.service.grouplesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.OrganizationRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.group.GroupNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateLessonException;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.SubdomainType;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.FileAccessPermissionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonCollisionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonFileValidatorService;
import com.github.sankowskiwojciech.courseslessons.stub.GroupLessonRequestStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentsGroupEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.SubdomainStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupLessonValidatorServiceTest {

    private final TutorRepository tutorRepositoryMock = mock(TutorRepository.class);
    private final StudentRepository studentRepositoryMock = mock(StudentRepository.class);
    private final SubdomainService subdomainServiceMock = mock(SubdomainService.class);
    private final OrganizationRepository organizationRepositoryMock = mock(OrganizationRepository.class);
    private final LessonCollisionValidatorService lessonCollisionValidatorServiceMock = mock(LessonCollisionValidatorService.class);
    private final LessonFileValidatorService lessonFileValidatorServiceMock = mock(LessonFileValidatorService.class);
    private final FileAccessPermissionValidatorService fileAccessPermissionValidatorServiceMock = mock(FileAccessPermissionValidatorService.class);
    private final GroupRepository groupRepositoryMock = mock(GroupRepository.class);
    private final GroupLessonValidatorService testee = new GroupLessonValidatorService(tutorRepositoryMock, subdomainServiceMock, organizationRepositoryMock, lessonCollisionValidatorServiceMock, lessonFileValidatorServiceMock, fileAccessPermissionValidatorServiceMock, groupRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(tutorRepositoryMock, studentRepositoryMock, subdomainServiceMock, organizationRepositoryMock, lessonCollisionValidatorServiceMock, lessonFileValidatorServiceMock, fileAccessPermissionValidatorServiceMock, groupRepositoryMock);
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
        when(groupRepositoryMock.findById(groupLessonRequestStub.getGroupId())).thenReturn(Optional.empty());

        //when
        GroupLesson groupLesson = testee.validateCreateGroupLessonRequest(groupLessonRequestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformation(groupLessonRequestStub.getSubdomainAlias());
        verify(organizationRepositoryMock).findById(subdomainStub.getEmailAddress());
        verify(tutorRepositoryMock).findById(groupLessonRequestStub.getTutorId());
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(groupLessonRequestStub.getStartDate(), groupLessonRequestStub.getEndDate(), tutorEntityStub.getEmailAddress());
        verify(lessonFileValidatorServiceMock).validateIfFileExists(anyString());
        verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(groupLessonRequestStub.getTutorId(), anyString());
        verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(groupLessonRequestStub.getSubdomainAlias(), tutorEntityStub.getEmailAddress());
        verify(groupRepositoryMock).findById(groupLessonRequestStub.getGroupId());
    }

    @Test(expected = UserNotAllowedToCreateLessonException.class)
    public void shouldThrowUserNotAllowedToCreateLessonExceptionWhenTutorIsNotTheOwnerOfTheGroup() {
        //given
        GroupLessonRequest groupLessonRequestStub = GroupLessonRequestStub.create();
        TutorEntity tutorEntityStub = TutorEntityStub.createWithTutorId(UUID.randomUUID().toString());
        GroupEntity groupEntityStub = StudentsGroupEntityStub.createWithTutorEntity(tutorEntityStub);
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(groupLessonRequestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(subdomainStub.getEmailAddress())).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(groupLessonRequestStub.getTutorId())).thenReturn(Optional.of(tutorEntityStub));
        when(groupRepositoryMock.findById(groupLessonRequestStub.getGroupId())).thenReturn(Optional.of(groupEntityStub));

        //when
        GroupLesson groupLesson = testee.validateCreateGroupLessonRequest(groupLessonRequestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformation(groupLessonRequestStub.getSubdomainAlias());
        verify(organizationRepositoryMock).findById(subdomainStub.getEmailAddress());
        verify(tutorRepositoryMock).findById(groupLessonRequestStub.getTutorId());
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(groupLessonRequestStub.getStartDate(), groupLessonRequestStub.getEndDate(), tutorEntityStub.getEmailAddress());
        verify(lessonFileValidatorServiceMock).validateIfFileExists(anyString());
        verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(groupLessonRequestStub.getTutorId(), anyString());
        verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(groupLessonRequestStub.getSubdomainAlias(), tutorEntityStub.getEmailAddress());
        verify(groupRepositoryMock).findById(groupLessonRequestStub.getGroupId());
    }

    @Test
    public void shouldDoNothingWhenGroupLessonRequestIsCorrect() {
        //given
        GroupLessonRequest groupLessonRequestStub = GroupLessonRequestStub.create();
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        GroupEntity groupEntityStub = StudentsGroupEntityStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(groupLessonRequestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(subdomainStub.getEmailAddress())).thenReturn(Optional.of(organizationEntityStub));
        when(tutorRepositoryMock.findById(groupLessonRequestStub.getTutorId())).thenReturn(Optional.of(tutorEntityStub));
        when(groupRepositoryMock.findById(groupLessonRequestStub.getGroupId())).thenReturn(Optional.of(groupEntityStub));

        //when
        GroupLesson groupLesson = testee.validateCreateGroupLessonRequest(groupLessonRequestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformation(groupLessonRequestStub.getSubdomainAlias());
        verify(organizationRepositoryMock).findById(subdomainStub.getEmailAddress());
        verify(tutorRepositoryMock).findById(groupLessonRequestStub.getTutorId());
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(groupLessonRequestStub.getStartDate(), groupLessonRequestStub.getEndDate(), tutorEntityStub.getEmailAddress());
        verify(lessonFileValidatorServiceMock).validateIfFileExists(anyString());
        verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(eq(groupLessonRequestStub.getTutorId()), anyString());
        verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(groupLessonRequestStub.getSubdomainAlias(), tutorEntityStub.getEmailAddress());
        verify(groupRepositoryMock).findById(groupLessonRequestStub.getGroupId());
        assertGroupLesson(groupLesson, groupLessonRequestStub, organizationEntityStub, tutorEntityStub, groupEntityStub);
    }

    private void assertGroupLesson(GroupLesson groupLesson, GroupLessonRequest groupLessonRequest, OrganizationEntity organizationEntity, TutorEntity tutorEntity, GroupEntity groupEntity) {
        assertNotNull(groupLesson);
        assertEquals(groupLessonRequest.getTitle(), groupLesson.getTitle());
        assertEquals(groupLessonRequest.getStartDate(), groupLesson.getStartDate());
        assertEquals(groupLessonRequest.getEndDate(), groupLesson.getEndDate());
        assertEquals(groupLessonRequest.getDescription(), groupLesson.getDescription());
        assertEquals(organizationEntity, groupLesson.getOrganizationEntity());
        assertEquals(tutorEntity, groupLesson.getTutorEntity());
        assertEquals(groupEntity, groupLesson.getGroupEntity());
    }
}