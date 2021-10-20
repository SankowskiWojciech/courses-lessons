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
        GroupLessonRequest requestStub = GroupLessonRequestStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        TutorEntity tutorStub = TutorEntityStub.create();
        OrganizationEntity organizationStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(subdomainStub.getEmailAddress())).thenReturn(Optional.of(organizationStub));
        when(tutorRepositoryMock.findById(requestStub.getTutorId())).thenReturn(Optional.of(tutorStub));
        when(groupRepositoryMock.findById(requestStub.getGroupId())).thenReturn(Optional.empty());

        //when
        GroupLesson lesson = testee.validateCreateGroupLessonRequest(requestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformation(requestStub.getSubdomainAlias());
        verify(organizationRepositoryMock).findById(subdomainStub.getEmailAddress());
        verify(tutorRepositoryMock).findById(requestStub.getTutorId());
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), tutorStub.getEmailAddress());
        verify(lessonFileValidatorServiceMock).validateIfFileExists(anyString());
        verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(requestStub.getTutorId(), anyString());
        verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(requestStub.getSubdomainAlias(), tutorStub.getEmailAddress());
        verify(groupRepositoryMock).findById(requestStub.getGroupId());
    }

    @Test(expected = UserNotAllowedToCreateLessonException.class)
    public void shouldThrowUserNotAllowedToCreateLessonExceptionWhenTutorIsNotTheOwnerOfTheGroup() {
        //given
        GroupLessonRequest requestStub = GroupLessonRequestStub.create();
        TutorEntity tutorStub = TutorEntityStub.createWithTutorId(UUID.randomUUID().toString());
        GroupEntity groupStub = StudentsGroupEntityStub.createWithTutorEntity(tutorStub);
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        OrganizationEntity organizationStub = OrganizationEntityStub.create();

        when(subdomainServiceMock.readSubdomainInformation(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(subdomainStub.getEmailAddress())).thenReturn(Optional.of(organizationStub));
        when(tutorRepositoryMock.findById(requestStub.getTutorId())).thenReturn(Optional.of(tutorStub));
        when(groupRepositoryMock.findById(requestStub.getGroupId())).thenReturn(Optional.of(groupStub));

        //when
        GroupLesson lesson = testee.validateCreateGroupLessonRequest(requestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformation(requestStub.getSubdomainAlias());
        verify(organizationRepositoryMock).findById(subdomainStub.getEmailAddress());
        verify(tutorRepositoryMock).findById(requestStub.getTutorId());
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), tutorStub.getEmailAddress());
        verify(lessonFileValidatorServiceMock).validateIfFileExists(anyString());
        verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(requestStub.getTutorId(), anyString());
        verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(requestStub.getSubdomainAlias(), tutorStub.getEmailAddress());
        verify(groupRepositoryMock).findById(requestStub.getGroupId());
    }

    @Test
    public void shouldDoNothingWhenGroupLessonRequestIsCorrect() {
        //given
        TutorEntity tutorStub = TutorEntityStub.create();
        GroupEntity groupStub = StudentsGroupEntityStub.create();
        Subdomain subdomainStub = SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION);
        OrganizationEntity organizationStub = OrganizationEntityStub.create();
        GroupLessonRequest requestStub = GroupLessonRequestStub.createWithSubdomainAlias(organizationStub.getAlias());

        when(subdomainServiceMock.readSubdomainInformation(requestStub.getSubdomainAlias())).thenReturn(subdomainStub);
        when(organizationRepositoryMock.findById(subdomainStub.getEmailAddress())).thenReturn(Optional.of(organizationStub));
        when(tutorRepositoryMock.findById(requestStub.getTutorId())).thenReturn(Optional.of(tutorStub));
        when(groupRepositoryMock.findById(requestStub.getGroupId())).thenReturn(Optional.of(groupStub));

        //when
        GroupLesson lesson = testee.validateCreateGroupLessonRequest(requestStub);

        //then
        verify(subdomainServiceMock).readSubdomainInformation(requestStub.getSubdomainAlias());
        verify(organizationRepositoryMock).findById(subdomainStub.getEmailAddress());
        verify(tutorRepositoryMock).findById(requestStub.getTutorId());
        verify(lessonCollisionValidatorServiceMock).validateIfNewLessonDoesNotCollideWithExistingOnes(requestStub.getStartDate(), requestStub.getEndDate(), tutorStub.getEmailAddress());
        verify(lessonFileValidatorServiceMock).validateIfFileExists(anyString());
        verify(fileAccessPermissionValidatorServiceMock).validateIfUserIsAllowedToAccessFile(eq(requestStub.getTutorId()), anyString());
        verify(subdomainServiceMock).validateIfUserIsAllowedToLoginToSubdomain(requestStub.getSubdomainAlias(), tutorStub.getEmailAddress());
        verify(groupRepositoryMock).findById(requestStub.getGroupId());
        assertGroupLesson(lesson, requestStub, organizationStub, tutorStub, groupStub);
    }

    private void assertGroupLesson(GroupLesson lesson, GroupLessonRequest request, OrganizationEntity organization, TutorEntity tutor, GroupEntity group) {
        assertNotNull(lesson);
        assertEquals(request.getTitle(), lesson.getTitle());
        assertEquals(request.getStartDate(), lesson.getStartDate());
        assertEquals(request.getEndDate(), lesson.getEndDate());
        assertEquals(request.getDescription(), lesson.getDescription());
        assertEquals(organization, lesson.getOrganizationEntity());
        assertEquals(tutor, lesson.getTutorEntity());
        assertEquals(group, lesson.getGroupEntity());
    }
}