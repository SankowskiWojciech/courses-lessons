package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileUserPermissionsEntity;
import org.junit.Test;

import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.FILE_ID_STUB;
import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileIdAndUserIdToFileUserPermissionsEntityTest {
    private final FileIdAndUserIdToFileUserPermissionsEntity testee = FileIdAndUserIdToFileUserPermissionsEntity.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        String fileIdStub = FILE_ID_STUB;
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;

        //when
        FileUserPermissionsEntity entity = testee.apply(fileIdStub, userIdStub);

        //then
        assertNotNull(entity);
        assertEquals(fileIdStub, entity.getFileUserPermissionsEntityId().getFileId());
        assertEquals(userIdStub, entity.getFileUserPermissionsEntityId().getUserId());
        assertTrue(entity.canRead());
        assertTrue(entity.canModify());
        assertTrue(entity.canDelete());
        assertNotNull(entity.getModificationDateTime());
        assertNotNull(entity.getModifiedBy());
    }
}