package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileUserPermissionsRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileUserPermissionsEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileUserPermissionsEntityId;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToAccessFileException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateFileException;
import com.github.sankowskiwojciech.coursestestlib.stub.FileUserPermissionsEntityStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.FILE_ID_STUB;
import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.STUDENT_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.USER_ID_STUB;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileUserPermissionsValidatorServiceImplTest {

    private final TutorRepository tutorRepositoryMock = mock(TutorRepository.class);
    private final StudentRepository studentRepositoryMock = mock(StudentRepository.class);
    private final FileUserPermissionsRepository fileUserPermissionsRepositoryMock = mock(FileUserPermissionsRepository.class);
    private final FileUserPermissionsValidatorService testee = new FileUserPermissionsValidatorServiceImpl(tutorRepositoryMock, studentRepositoryMock, fileUserPermissionsRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(tutorRepositoryMock, studentRepositoryMock, fileUserPermissionsRepositoryMock);
    }

    @Test
    public void shouldDoNothingWhenUserIdBelongsToTutor() {
        //given
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        boolean userExists = true;
        when(tutorRepositoryMock.existsById(eq(userIdStub))).thenReturn(userExists);

        //when
        testee.validateIfUserIsAllowedToCreateFile(userIdStub);

        //then nothing happens
        verify(tutorRepositoryMock).existsById(eq(userIdStub));
    }

    @Test
    public void shouldDoNothingWhenUserIdBelongsToStudent() {
        //given
        String userIdStub = STUDENT_EMAIL_ADDRESS_STUB;
        boolean userExists = true;
        when(tutorRepositoryMock.existsById(eq(userIdStub))).thenReturn(!userExists);
        when(studentRepositoryMock.existsById(eq(userIdStub))).thenReturn(userExists);

        //when
        testee.validateIfUserIsAllowedToCreateFile(userIdStub);

        //then nothing happens
        verify(tutorRepositoryMock).existsById(eq(userIdStub));
        verify(studentRepositoryMock).existsById(eq(userIdStub));
    }

    @Test(expected = UserNotAllowedToCreateFileException.class)
    public void shouldThrowUserNotAllowedToCreateFileExceptionWhenUserIsNotTutorAndIsNotStudent() {
        //given
        String userIdStub = USER_ID_STUB;
        boolean userExists = false;
        when(tutorRepositoryMock.existsById(eq(userIdStub))).thenReturn(userExists);
        when(studentRepositoryMock.existsById(eq(userIdStub))).thenReturn(userExists);

        //when
        try {
            testee.validateIfUserIsAllowedToCreateFile(userIdStub);
        } catch (UserNotAllowedToCreateFileException e) {
            //then exception is thrown
            verify(tutorRepositoryMock).existsById(eq(userIdStub));
            verify(studentRepositoryMock).existsById(eq(userIdStub));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToAccessFileException.class)
    public void shouldThrowUserNotAllowedToReadFileWhenUserIsNotAllowedToReadFile() {
        //given
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        String fileIdStub = FILE_ID_STUB;
        FileUserPermissionsEntity fileUserPermissionsEntityStub = FileUserPermissionsEntityStub.create(fileIdStub, userIdStub, false, true, true);

        when(fileUserPermissionsRepositoryMock.findById(any(FileUserPermissionsEntityId.class))).thenReturn(Optional.of(fileUserPermissionsEntityStub));

        //when
        try {
            testee.validateIfUserIsAllowedToAccessFile(userIdStub, fileIdStub);
        } catch (UserNotAllowedToAccessFileException e) {

            //then exception is thrown
            verify(fileUserPermissionsRepositoryMock).findById(any(FileUserPermissionsEntityId.class));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToAccessFileException.class)
    public void shouldThrowUserNotAllowedToReadFileWhenUserIsNotPresentInTableFileUserPermissionsForThisFile() {
        //given
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        String fileIdStub = FILE_ID_STUB;

        when(fileUserPermissionsRepositoryMock.findById(any(FileUserPermissionsEntityId.class))).thenReturn(Optional.empty());

        //when
        try {
            testee.validateIfUserIsAllowedToAccessFile(userIdStub, fileIdStub);
        } catch (UserNotAllowedToAccessFileException e) {

            //then exception is thrown
            verify(fileUserPermissionsRepositoryMock).findById(any(FileUserPermissionsEntityId.class));
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenUserIsAllowedToReadFile() {
        //given
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        String fileIdStub = FILE_ID_STUB;
        FileUserPermissionsEntity fileUserPermissionsEntityStub = FileUserPermissionsEntityStub.create(fileIdStub, userIdStub, true, true, true);

        when(fileUserPermissionsRepositoryMock.findById(any(FileUserPermissionsEntityId.class))).thenReturn(Optional.of(fileUserPermissionsEntityStub));

        //when
        testee.validateIfUserIsAllowedToAccessFile(userIdStub, fileIdStub);

        //then nothing happens
        verify(fileUserPermissionsRepositoryMock).findById(any(FileUserPermissionsEntityId.class));
    }
}