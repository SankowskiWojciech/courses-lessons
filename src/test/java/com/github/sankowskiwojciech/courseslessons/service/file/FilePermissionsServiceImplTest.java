package com.github.sankowskiwojciech.courseslessons.service.file;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileUserPermissionsRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentGroupAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileUserPermissionsEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileUserPermissionsEntityId;
import com.github.sankowskiwojciech.coursescorelib.model.db.group.StudentGroupAccessEntity;
import com.github.sankowskiwojciech.coursestestlib.stub.FileUserPermissionsEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.StudentGroupAccessEntityStub;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.FILE_ID_STUB;
import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.GROUP_ID_STUB;
import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.USER_ID_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FilePermissionsServiceImplTest {
    private final FileUserPermissionsRepository fileUserPermissionsRepositoryMock = mock(FileUserPermissionsRepository.class);
    private final StudentGroupAccessRepository studentGroupAccessRepositoryMock = mock(StudentGroupAccessRepository.class);
    private final FilePermissionsService testee = new FilePermissionsServiceImpl(fileUserPermissionsRepositoryMock, studentGroupAccessRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(fileUserPermissionsRepositoryMock, studentGroupAccessRepositoryMock);
    }

    @Test
    public void shouldReadIdsOfFilesToWhichUserHasAccessCorrectly() {
        //given
        String userIdStub = USER_ID_STUB;
        String fileIdStub = FILE_ID_STUB;
        List<FileUserPermissionsEntity> fileUserPermissionsEntitiesStub = Lists.newArrayList(FileUserPermissionsEntityStub.createWithFullAccess(fileIdStub, userIdStub));

        when(fileUserPermissionsRepositoryMock.findAllByFileUserPermissionsEntityIdUserIdAndCanReadIsTrue(userIdStub)).thenReturn(fileUserPermissionsEntitiesStub);

        //when
        Set<String> filesIdsToWhichUserHasAccess = testee.readIdsOfFilesToWhichUserHasAccess(userIdStub);

        //then
        verify(fileUserPermissionsRepositoryMock).findAllByFileUserPermissionsEntityIdUserIdAndCanReadIsTrue(userIdStub);

        assertNotNull(filesIdsToWhichUserHasAccess);
        assertFalse(filesIdsToWhichUserHasAccess.isEmpty());
        assertEquals(1, filesIdsToWhichUserHasAccess.size());
        assertEquals(fileIdStub, filesIdsToWhichUserHasAccess.stream().findFirst().get());
    }

    @Test
    public void shouldAddUserPermissionsToFileCorrectlyWhenFileUserPermissionsAlreadyExist() {
        //given
        boolean exists = true;
        String userId = USER_ID_STUB;
        String fileId = FILE_ID_STUB;

        when(fileUserPermissionsRepositoryMock.existsById(any(FileUserPermissionsEntityId.class))).thenReturn(exists);

        //when
        testee.addUserPermissionsToFile(userId, fileId);

        //then nothing happens
        verify(fileUserPermissionsRepositoryMock).existsById(any(FileUserPermissionsEntityId.class));
    }

    @Test
    public void shouldAddUserPermissionsToFileCorrectlyWhenFileUserPermissionsDoesNotExist() {
        //given
        boolean exists = false;
        String userId = USER_ID_STUB;
        String fileId = FILE_ID_STUB;

        when(fileUserPermissionsRepositoryMock.existsById(any(FileUserPermissionsEntityId.class))).thenReturn(exists);

        //when
        testee.addUserPermissionsToFile(userId, fileId);

        //then nothing happens
        verify(fileUserPermissionsRepositoryMock).existsById(any(FileUserPermissionsEntityId.class));
        verify(fileUserPermissionsRepositoryMock).save(any(FileUserPermissionsEntity.class));
    }

    @Test
    public void shouldAddUserPermissionsToFilesToStudentsFromGroupCorrectly() {
        //given
        List<StudentGroupAccessEntity> studentGroupAccessEntitiesStub = Collections.singletonList(StudentGroupAccessEntityStub.create());
        Collection<String> filesIdsStub = Collections.singleton(FILE_ID_STUB);
        String groupIdStub = GROUP_ID_STUB;

        when(studentGroupAccessRepositoryMock.findAllByStudentGroupAccessEntityIdGroupId(groupIdStub)).thenReturn(studentGroupAccessEntitiesStub);
        when(fileUserPermissionsRepositoryMock.existsById(any(FileUserPermissionsEntityId.class))).thenReturn(false);

        //when
        testee.addUserPermissionsToFilesToStudentsFromGroup(groupIdStub, filesIdsStub);

        //then nothing happens
        verify(studentGroupAccessRepositoryMock).findAllByStudentGroupAccessEntityIdGroupId(groupIdStub);
        verify(fileUserPermissionsRepositoryMock).existsById(any(FileUserPermissionsEntityId.class));
        verify(fileUserPermissionsRepositoryMock).save(any(FileUserPermissionsEntity.class));
    }
}
