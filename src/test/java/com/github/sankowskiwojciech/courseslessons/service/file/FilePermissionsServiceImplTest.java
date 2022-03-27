package com.github.sankowskiwojciech.courseslessons.service.file;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileUserPermissionsRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileUserPermissionsEntity;
import com.github.sankowskiwojciech.coursestestlib.stub.FileUserPermissionsEntityStub;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.FILE_ID_STUB;
import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.USER_ID_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FilePermissionsServiceImplTest {
    private final FileUserPermissionsRepository fileUserPermissionsRepositoryMock = mock(FileUserPermissionsRepository.class);
    private final FilePermissionsService testee = new FilePermissionsServiceImpl(fileUserPermissionsRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(fileUserPermissionsRepositoryMock);
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
}
